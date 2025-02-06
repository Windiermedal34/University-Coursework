#include "my-packet-sink.h"
#include "MyQueue.h"
#include "PriorityTag.h"
// #include "MySocket.h"
// #include "Queue2.h"
#include "ns3/queue.h"
// #include "ns3/queue-fwd.h"
#include "ns3/address-utils.h"
#include "ns3/address.h"
#include "ns3/boolean.h"
#include "ns3/inet-socket-address.h"
#include "ns3/inet6-socket-address.h"
#include "ns3/ipv4-packet-info-tag.h"
#include "ns3/ipv6-packet-info-tag.h"
#include "ns3/log.h"
#include "ns3/node.h"
#include "ns3/packet.h"
#include "ns3/simulator.h"
#include "ns3/socket-factory.h"
#include "ns3/socket.h"
#include "ns3/trace-source-accessor.h"
#include "ns3/udp-socket-factory.h"
#include "ns3/udp-socket.h"
#include "thread.h"

#include <stdio.h>      /* printf, scanf, puts, NULL */
#include <stdlib.h>     /* srand, rand */
#include <time.h> 

#include <cstdlib>
#include <iostream>
#include <ctime>
namespace ns3
{

NS_LOG_COMPONENT_DEFINE("MyPacketSink");

NS_OBJECT_ENSURE_REGISTERED(MyPacketSink);

TypeId
MyPacketSink::GetTypeId()
{
    static TypeId tid =
        TypeId("ns3::MyPacketSink")
            .SetParent<Application>()
            .SetGroupName("Applications")
            .AddConstructor<MyPacketSink>()
            .AddAttribute("Local",
                          "The Address on which to Bind the rx socket.",
                          AddressValue(),
                          MakeAddressAccessor(&MyPacketSink::m_local),
                          MakeAddressChecker())
            .AddAttribute("Protocol",
                          "The type id of the protocol to use for the rx socket.",
                          TypeIdValue(UdpSocketFactory::GetTypeId()),
                          MakeTypeIdAccessor(&MyPacketSink::m_tid),
                          MakeTypeIdChecker())
            .AddAttribute("EnableSeqTsSizeHeader",
                          "Enable optional header tracing of SeqTsSizeHeader",
                          BooleanValue(false),
                          MakeBooleanAccessor(&MyPacketSink::m_enableSeqTsSizeHeader),
                          MakeBooleanChecker())
            .AddTraceSource("Rx",
                            "A packet has been received",
                            MakeTraceSourceAccessor(&MyPacketSink::m_rxTrace),
                            "ns3::Packet::AddressTracedCallback")
            .AddTraceSource("RxWithAddresses",
                            "A packet has been received",
                            MakeTraceSourceAccessor(&MyPacketSink::m_rxTraceWithAddresses),
                            "ns3::Packet::TwoAddressTracedCallback")
            .AddTraceSource("RxWithSeqTsSize",
                            "A packet with SeqTsSize header has been received",
                            MakeTraceSourceAccessor(&MyPacketSink::m_rxTraceWithSeqTsSize),
                            "ns3::MyPacketSink::SeqTsSizeCallback");
    return tid;
}

MyPacketSink::MyPacketSink()
  : m_socket(0),
    m_totalRx(0),
    counter(0),
    m_enqueueTime(0),
    m_dequeueTime(0)
{
    NS_LOG_FUNCTION(this);
}

MyPacketSink::~MyPacketSink()
{
    NS_LOG_FUNCTION(this);
}

uint64_t
MyPacketSink::GetTotalRx() const
{
    NS_LOG_FUNCTION(this);
    return m_totalRx;
}

Ptr<Socket>
MyPacketSink::GetListeningSocket() const
{
    NS_LOG_FUNCTION(this);
    return m_socket;
}

std::list<Ptr<Socket>>
MyPacketSink::GetAcceptedSockets() const
{
    NS_LOG_FUNCTION(this);
    return m_socketList;
}

// std::list<Ptr<Packet>>
// MyPacketSink::GetPackets() const
// {
//     NS_LOG_FUNCTION(this);
//     return m_packets;
// }

void
MyPacketSink::DoDispose()
{
    NS_LOG_FUNCTION(this);
    m_socket = nullptr;
    m_socketList.clear();

    // chain up
    Application::DoDispose();
}

// Application Methods
void
MyPacketSink::StartApplication() // Called at time specified by Start
{
    Ptr<SystemThread> thread = Create<SystemThread> (MakeCallback(&MyPacketSink::ProcessPacket, this));
    thread->Start();
    
    NS_LOG_FUNCTION(this);
    // Create the socket if not already
    if (!m_socket)
    {
        m_socket = Socket::CreateSocket(GetNode(), m_tid);
        if (m_socket->Bind(m_local) == -1)
        {
            NS_FATAL_ERROR("Failed to bind socket");
        }
        m_socket->Listen();
        m_socket->ShutdownSend();
        if (addressUtils::IsMulticast(m_local))
        {
            Ptr<UdpSocket> udpSocket = DynamicCast<UdpSocket>(m_socket);
            if (udpSocket)
            {
                // equivalent to setsockopt (MCAST_JOIN_GROUP)
                udpSocket->MulticastJoinGroup(0, m_local);
            }
            else
            {
                NS_FATAL_ERROR("Error: joining multicast on a non-UDP socket");
            }
        }
    }

    if (InetSocketAddress::IsMatchingType(m_local))
    {
        m_localPort = InetSocketAddress::ConvertFrom(m_local).GetPort();
    }
    else if (Inet6SocketAddress::IsMatchingType(m_local))
    {
        m_localPort = Inet6SocketAddress::ConvertFrom(m_local).GetPort();
    }
    else
    {
        m_localPort = 0;
    }
    m_socket->SetRecvCallback(MakeCallback(&MyPacketSink::HandleRead, this));
    m_socket->SetRecvPktInfo(true);
    m_socket->SetAcceptCallback(MakeNullCallback<bool, Ptr<Socket>, const Address&>(),
                                MakeCallback(&MyPacketSink::HandleAccept, this));
    m_socket->SetCloseCallbacks(MakeCallback(&MyPacketSink::HandlePeerClose, this),
                                MakeCallback(&MyPacketSink::HandlePeerError, this));
    thread->Join();
}

void
MyPacketSink::StopApplication() // Called at time specified by Stop
{
    NS_LOG_FUNCTION(this);
    while (!m_socketList.empty()) // these are accepted sockets, close them
    {
        Ptr<Socket> acceptedSocket = m_socketList.front();
        m_socketList.pop_front();
        acceptedSocket->Close();
    }
    if (m_socket)
    {
        m_socket->Close();
        m_socket->SetRecvCallback(MakeNullCallback<void, Ptr<Socket>>());
    }
}

void
MyPacketSink::SetEnqueueTime(const double& time)
{
    m_enqueueTime = time;
    enqueueTimes.push_back(time);
}

void
MyPacketSink::SetHighEnqueue(const double& time)
{
    m_highEnqueue = time;
    highEnqueueList.push_back(time);
}

void
MyPacketSink::SetDequeueTime(const double& time)
{
    m_dequeueTime = time;
    dequeueTimes.push_back(time);
}

void
MyPacketSink::SetHighDequeue(const double& time)
{
    m_highDequeue = time;
    highDequeueList.push_back(time);
}

std::list<double>
MyPacketSink::GetEnqueueTimeList() const
{
    return enqueueTimes;
}

std::list<double>
MyPacketSink::GetDequeueTimeList() const
{
    return dequeueTimes;
}

std::list<double>
MyPacketSink::GetHighEnqueueList() const
{
    return highEnqueueList;
}

std::list<double>
MyPacketSink::GetHighDequeueList() const
{
    return highDequeueList;;
}

uint64_t
MyPacketSink::GetCounter() const
{
    return counter;
}

uint64_t
MyPacketSink::GetLogicalTime() const
{
    return m_logicalTime;
}

void
MyPacketSink::ProcessPacket()
{
    std::srand(std::time(nullptr));
    double randomNumber = std::rand() & 9900 + 100;

    // PriorityTag tag;
    // double priority = randomNumber;
    // tag.SetPriority(priority);
    // packet->RemovePacketTag();
    // packet->AddPacketTag(tag);

    Ptr<Packet> pkt = queue.Dequeue();
    
    // uint64_t packetSize = 512;
    uint64_t timeChange = randomNumber/100;
    m_logicalTime += timeChange;
    
    SetDequeueTime(m_logicalTime);
}

void
MyPacketSink::HandleRead(Ptr<Socket> socket)
{
    NS_LOG_FUNCTION(this << socket);
    Ptr<Packet> packet;
    Address from;
    Address localAddress;

    // uint64_t mod = GetTotalRx();
    // mod = mod/512;
    // if((mod)%5)
    // {
    //     queue.Enqueue(packet);
    //     SetEnqueueTime(Simulator::Now().GetSeconds());
    // }
    // else
    // {
    //     highQueue.Enqueue(packet);
    //     SetHighEnqueue(Simulator::Now().GetSeconds());
    //     counter += 1;
    // }
    // if(socket->GetPriority() == 1){
        
    // }
    // else if(socket->GetPriority() == 0)
    // {
        
    // }

    // highQueue.Enqueue(packet);
    // SetHighEnqueue(Simulator::Now().GetSeconds());

    std::srand(std::time(nullptr));
    double randomNumber = std::rand() & 9900 + 100;

    // PriorityTag tag;
    // double priority = randomNumber;
    // tag.SetPriority(priority);
    // packet->RemovePacketTag();
    // packet->AddPacketTag(tag);

    queue.Enqueue(packet);
    
    // uint64_t packetSize = 512;
    uint64_t timeChange = randomNumber/100;
    m_logicalTime += timeChange;
    
    SetEnqueueTime(m_logicalTime);

    while ((packet = socket->RecvFrom(from)))
    {
        if (packet->GetSize() == 0)
        { // EOF8  jhz 
            break;
        }
        m_totalRx += packet->GetSize();
        if (InetSocketAddress::IsMatchingType(from))
        {
            NS_LOG_INFO("At time " << Simulator::Now().As(Time::S) << " packet sink received "
                                   << packet->GetSize() << " bytes from "
                                   << InetSocketAddress::ConvertFrom(from).GetIpv4() << " port "
                                   << InetSocketAddress::ConvertFrom(from).GetPort() << " total Rx "
                                   << m_totalRx << " bytes");
        }
        m_rxTrace(packet, from);
    }
}

void
MyPacketSink::PacketReceived(const Ptr<Packet>& p, const Address& from, const Address& localAddress)
{
    SeqTsSizeHeader header;
    Ptr<Packet> buffer;

    Ptr<Packet> pkt;

    // if(!highQueue.IsEmpty()){
    //     pkt = highQueue.Dequeue();
    //     SetDequeueTime(Simulator::Now().GetSeconds());
    // }
    // else{
    //     pkt = queue.Dequeue();
    //     SetDequeueTime(Simulator::Now().GetSeconds());
    // }
    
    // SetServerTime(GetEnqueueTime(), GetDequeueTime());

    auto itBuffer = m_buffer.find(from);
    if (itBuffer == m_buffer.end())
    {
        itBuffer = m_buffer.insert(std::make_pair(from, Create<Packet>(0))).first;
    }

    buffer = itBuffer->second;
    buffer->AddAtEnd(pkt);
    buffer->PeekHeader(header);

    NS_ABORT_IF(header.GetSize() == 0);

    while (buffer->GetSize() >= header.GetSize())
    {
        NS_LOG_DEBUG("Removing packet of size " << header.GetSize() << " from buffer of size "
                                                << buffer->GetSize());
        Ptr<Packet> complete = buffer->CreateFragment(0, static_cast<uint32_t>(header.GetSize()));
        buffer->RemoveAtStart(static_cast<uint32_t>(header.GetSize()));

        complete->RemoveHeader(header);

        m_rxTraceWithSeqTsSize(complete, from, localAddress, header);

        if (buffer->GetSize() > header.GetSerializedSize())
        {
            buffer->PeekHeader(header);
        }
        else
        {
            break;
        }
    }
}

std::list<double>
MyPacketSink::GetAllServerTimes() const
{
    return allServerTimes;
}

void
MyPacketSink::SetServerTime(const double& m_enqueueTime, const double& m_dequeueTime)
{
    serverTime = m_dequeueTime - m_enqueueTime;
    allServerTimes.push_back(serverTime);
}

void
MyPacketSink::SetHighServer(const double& m_highEnqueue, const double& m_highDequeue)
{
    highServer = m_highDequeue - m_highEnqueue;
}

double
MyPacketSink::GetServerTime() const
{
    return serverTime;
}

double
MyPacketSink::GetHighServer() const
{
    return highServer;
}

double
MyPacketSink::GetEnqueueTime() const
{
    return m_enqueueTime;
}

double
MyPacketSink::GetHighEnqueue() const
{
    return m_highEnqueue;
}

double
MyPacketSink::GetDequeueTime() const
{
    return m_dequeueTime;
}

double
MyPacketSink::GetHighDequeue() const
{
    return m_highDequeue;
}

void
MyPacketSink::HandlePeerClose(Ptr<Socket> socket)
{
    NS_LOG_FUNCTION(this << socket);
}

void
MyPacketSink::HandlePeerError(Ptr<Socket> socket)
{
    NS_LOG_FUNCTION(this << socket);
}

void
MyPacketSink::HandleAccept(Ptr<Socket> s, const Address& from)
{
    NS_LOG_FUNCTION(this << s << from);
    s->SetRecvCallback(MakeCallback(&MyPacketSink::HandleRead, this));
    m_socketList.push_back(s);
}

} // Namespace ns3
