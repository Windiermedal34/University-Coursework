
#include "ns3/applications-module.h"
#include "my-packet-sink-helper.h"
#include "ns3/core-module.h"
#include "ns3/internet-module.h"
#include "ns3/netanim-module.h"
#include "ns3/network-module.h"
#include "ns3/point-to-point-layout-module.h"
#include "ns3/point-to-point-module.h"
#include "PriorityTag.h"
#include "my-on-off-helper.h"
#include "my-on-off-application.h"
#include "my-packet-sink.h"
#include "MyQueue.h"
#include "ns3/traced-value.h"
#include "ns3/trace-source-accessor.h"
#include <iostream>


#include "ns3/flow-monitor-helper.h"
#include "ns3/traffic-control-module.h"

#include <stdio.h>      /* printf, scanf, puts, NULL */
#include <stdlib.h>     /* srand, rand */
#include <time.h> 

using namespace ns3;

NS_LOG_COMPONENT_DEFINE("MainClass");

static void
Rx(Ptr<const Packet> p, const Address& a, const Address& to, const SeqTsSizeHeader& m_rxTraceWithSeqTsSize)
{
    NS_LOG_UNCOND("Rx at " << Simulator::Now().GetSeconds() << " from " << a << " to " << to << " header " << m_rxTraceWithSeqTsSize);
}

static void
RxShort(Ptr<const Packet> p, const Address& a)
{
    NS_LOG_UNCOND("Rx at " << Simulator::Now().GetSeconds() << " from " << a);
}

int
main(int argc, char* argv[])
{
    //
    // Set up some default values for the simulation.
    //
    // Config::SetDefault("ns3::OnOffApplication::PacketSize", UintegerValue(137));

    // ??? try and stick 15kb/s into the data rate
    // Config::SetDefault("ns3::OnOffApplication::DataRate", StringValue("25kb/s"));

    // Config::SetDefault("ns3::OnOffApplication::MaxBytes", UintegerValue(maxBytes));

    //
    // Default number of nodes in the star.  Overridable by command line argument.
    //
    uint32_t nSpokes = 60;

    CommandLine cmd(__FILE__);
    cmd.AddValue("nSpokes", "Number of nodes to place in the star", nSpokes);
    cmd.Parse(argc, argv);

    NS_LOG_INFO("Build star topology.");
    PointToPointHelper pointToPoint;
    pointToPoint.SetDeviceAttribute("DataRate", StringValue("5Mbps"));
    pointToPoint.SetChannelAttribute("Delay", StringValue("2ms"));
    pointToPoint.SetQueue("ns3::DropTailQueue", "MaxSize", StringValue("100p"));
    PointToPointStarHelper star(nSpokes, pointToPoint);

    TrafficControlHelper tch;
    uint16_t handle = tch.SetRootQueueDisc("ns3::PrioQueueDisc", "Priomap", StringValue("0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1"));
    TrafficControlHelper::ClassIdList cid = tch.AddQueueDiscClasses(handle, 2, "ns3::QueueDiscClass");
    tch.AddChildQueueDisc(handle, cid[0], "ns3::FifoQueueDisc");
    tch.AddChildQueueDisc(handle, cid[1], "ns3::RedQueueDisc");

    NS_LOG_INFO("Install internet stack on all nodes.");
    InternetStackHelper internet;
    star.InstallStack(internet);

    NS_LOG_INFO("Assign IP Addresses.");
    star.AssignIpv4Addresses(Ipv4AddressHelper("10.1.1.0", "255.255.255.0"));

    NS_LOG_INFO("Create applications.");
    //
    // Create a packet sink on the star "hub" to receive packets.
    //

    Ptr<MyPacketSink> mps = CreateObject<MyPacketSink>();
    mps->TraceConnectWithoutContext("RxWithSeqTsSize", MakeCallback(&Rx));
    // mps->TraceConnectWithoutContext("Rx", MakeCallback(&RxShort));

    uint16_t port = 50000;
    Address hubLocalAddress(InetSocketAddress(Ipv4Address::GetAny(), port));
    MyPacketSinkHelper myPacketSinkHelper("ns3::TcpSocketFactory", hubLocalAddress);
    myPacketSinkHelper.SetAttribute("EnableSeqTsSizeHeader", BooleanValue(true));

    // Ptr<MyPacketSink> mps = CreateObject<MyPacketSink>();
    // // mps->TraceConnectWithoutContext("RxWithSeqTsSize", MakeCallback(&Rx));
    // mps->TraceConnectWithoutContext("Rx", MakeCallback(&RxShort));
    
    ApplicationContainer hubApp = myPacketSinkHelper.Install(star.GetHub());
    hubApp.Start(Seconds(1.0));
    hubApp.Stop(Seconds(25.0));

    //
    // Create OnOff applications to send TCP to the hub, one on each spoke node.
    //
    uint32_t bytesPerPacket = 512;
    uint32_t packetsPerNode = 500;
    uint32_t maxBytes = packetsPerNode * bytesPerPacket;
    

    Ptr<Packet> pkt = Create<Packet> (1024);
    PriorityTag tag;
    double priority = 1;
    tag.SetPriority(priority);
    pkt->AddPacketTag(tag);
    
    // tag1.SetFlowId (1);
    // highPacket->AddPacketTag (tag1);

    // Ptr<Socket> ns3TcpSocket = Socket::CreateSocket(star.GetSpokeNode(0), TcpSocketFactory::GetTypeId());
    // ns3TcpSocket->SendTo(highPacket, flags, hubLocalAddress);

    // Config::SetDefault("ns3::Packet::AddPacketTag", UintegerValue(maxBytes));

    MyOnOffHelper onOffHelperBackup("ns3::MyOnOffApplicationBackup", "ns3::TcpSocketFactory", Address());
    onOffHelperBackup.SetAttribute("OnTime", StringValue("ns3::ConstantRandomVariable[Constant=1]"));
    onOffHelperBackup.SetAttribute("OffTime", StringValue("ns3::ConstantRandomVariable[Constant=0]"));
    onOffHelperBackup.SetAttribute("PacketSize", UintegerValue(bytesPerPacket));
    onOffHelperBackup.SetAttribute("DataRate", StringValue("50kb/s"));
    onOffHelperBackup.SetAttribute("MaxBytes", UintegerValue(maxBytes));

    MyOnOffHelper myOnOffHelper("ns3::MyOnOffApplication", "ns3::TcpSocketFactory", Address());
    myOnOffHelper.SetAttribute("OnTime", StringValue("ns3::ConstantRandomVariable[Constant=1]"));
    myOnOffHelper.SetAttribute("OffTime", StringValue("ns3::ConstantRandomVariable[Constant=0]"));
    myOnOffHelper.SetAttribute("DataRate", StringValue("50kb/s"));
    myOnOffHelper.SetAttribute("MaxBytes", UintegerValue(maxBytes));

    ApplicationContainer spokeApps;

    for (uint32_t i = 0; i < star.SpokeCount(); ++i)
    {
        AddressValue remoteAddress(InetSocketAddress(star.GetHubIpv4Address(i), port));
        if((i+1) % 5){
            myOnOffHelper.SetAttribute("Remote", remoteAddress);
            spokeApps.Add(myOnOffHelper.Install(star.GetSpokeNode(i)));
        }
        else
        {
            onOffHelperBackup.SetAttribute("Remote", remoteAddress);
            spokeApps.Add(onOffHelperBackup.Install(star.GetSpokeNode(i)));
        }
    }
    spokeApps.Start(Seconds(2.0));
    spokeApps.Stop(Seconds(25.0));

    NS_LOG_INFO("Enable static global routing.");
    //
    // Turn on global static routing so we can actually be routed across the star.
    //
    Ipv4GlobalRoutingHelper::PopulateRoutingTables();

    NS_LOG_INFO("Enable pcap tracing.");
    //
    // Do pcap tracing on all point-to-point devices on all nodes.
    //
    pointToPoint.EnablePcapAll("Main");

    NS_LOG_INFO("Run Simulation.");
    std::cout << "Running the simulation" << std::endl;
    Simulator::Run();
    Simulator::Destroy();
    NS_LOG_INFO("Done.");
    std::cout << std::endl << "*** Application statistics ***" << std::endl;
    uint64_t totalPacketsThr = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetTotalRx();
    std::list<double> enq = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetEnqueueTimeList();
    std::list<double> dequeue = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetDequeueTimeList();
    std::list<double> highDequeue = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetHighDequeueList();
    std::list<double> highEnq = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetHighEnqueueList();
    uint64_t counter = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetCounter();
    double server = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetServerTime();
    std::list<double> servers = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetAllServerTimes();
    uint64_t time = DynamicCast<MyPacketSink>(hubApp.Get(0))->GetLogicalTime();

    std::srand(std::time(nullptr));
    uint64_t randomNumber = std::rand() & 9900 + 100;
    // randomNumber = rand() % 9900 + 100;

    uint64_t totalPackets = totalPacketsThr/bytesPerPacket;
    std::cout << "  Rx Bytes: " << totalPacketsThr << std::endl;
    std::cout << "  Rx Packets: " << totalPackets << std::endl;
    std::cout << "  Rx Packets/Client: " << totalPackets/nSpokes << std::endl;
    // std::cout << pkt->GetSize() << std::endl;
    // std::cout << pkt->PeekPacketTag(tag) << std::endl;
    std::cout << " Packets enqueued to standard queue total: " << enq.size() << std::endl;
    // std::cout << enq.front() << std::endl;
    // std::cout << enq.back() << std::endl;
    std::cout << " Packets dequeued to standard queue total: " << dequeue.size() << std::endl;
    // std::cout << dequeue.front() << std::endl;
    // std::cout << dequeue.back() << std::endl;
    std::cout << " Packets enqueued to high priority queue total: " << highEnq.size() << std::endl;
    std::cout << " Packets dequeued to high priority queue total: " << highDequeue.size() << std::endl;
    std::cout << " Server time: " << servers.size() << std::endl;
    std::cout << " Server time: " << servers.front() << std::endl;
    std::cout << " Server time: " << servers.back() << std::endl;
    std::cout << " Packets not dequeued from high priority queue for unknown reason: " << counter << std::endl;
    std::cout << " Time: " << time << std::endl;
    std::cout << " Random Number: " << randomNumber << " Max: " << RAND_MAX << std::endl;
    std::cout << " Time: " << 100 + std::rand()/((RAND_MAX + 1u)/9900) << std::endl;

    return 0;
}
