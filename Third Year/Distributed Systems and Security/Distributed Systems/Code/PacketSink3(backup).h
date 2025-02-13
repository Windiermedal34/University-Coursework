#ifndef PACKET_SINK_3_H
#define PACKET_SINK_3_H

#include "ns3/address.h"
#include "ns3/application.h"
#include "ns3/event-id.h"
#include "ns3/inet-socket-address.h"
#include "ns3/inet6-socket-address.h"
#include "ns3/ptr.h"
#include "ns3/seq-ts-size-header.h"
#include "ns3/traced-callback.h"

#include <unordered_map>

namespace ns3
{

class Address;
class Socket;
class Packet;

class PacketSink3 : public Application
{
  public:
    /**
     * \brief Get the type ID.
     * \return the object TypeId
     */
    static TypeId GetTypeId();
    PacketSink3();

    ~PacketSink3() override;

    /**
     * \return the total bytes received in this sink app
     */
    uint64_t GetTotalRx() const;

    /**
     * \return pointer to listening socket
     */
    Ptr<Socket> GetListeningSocket() const;

    /**
     * \return list of pointers to accepted sockets
     */
    std::list<Ptr<Socket>> GetAcceptedSockets() const;

    /**
     * TracedCallback signature for a reception with addresses and SeqTsSizeHeader
     *
     * \param p The packet received (without the SeqTsSize header)
     * \param from From address
     * \param to Local address
     * \param header The SeqTsSize header
     */
    typedef void (*SeqTsSizeCallback)(Ptr<const Packet> p,
                                      const Address& from,
                                      const Address& to,
                                      const SeqTsSizeHeader& header);

    uint64_t GetCount() const;

  protected:
    void DoDispose() override;

  private:
    // inherited from Application base class.
    void StartApplication() override; // Called at time specified by Start
    void StopApplication() override;  // Called at time specified by Stop

    /**
     * \brief Handle a packet received by the application
     * \param socket the receiving socket
     */
    void HandleRead(Ptr<Socket> socket);
    /**
     * \brief Handle an incoming connection
     * \param socket the incoming connection socket
     * \param from the address the connection is from
     */
    void HandleAccept(Ptr<Socket> socket, const Address& from);
    /**
     * \brief Handle an connection close
     * \param socket the connected socket
     */
    void HandlePeerClose(Ptr<Socket> socket);
    /**
     * \brief Handle an connection error
     * \param socket the connected socket
     */
    void HandlePeerError(Ptr<Socket> socket);

    /**
     * \brief Packet received: assemble byte stream to extract SeqTsSizeHeader
     * \param p received packet
     * \param from from address
     * \param localAddress local address
     *
     * The method assembles a received byte stream and extracts SeqTsSizeHeader
     * instances from the stream to export in a trace source.
     */
    void PacketReceived(const Ptr<Packet>& p, const Address& from, const Address& localAddress);

    /**
     * \brief Hashing for the Address class
     */
    struct AddressHash
    {
        /**
         * \brief operator ()
         * \param x the address of which calculate the hash
         * \return the hash of x
         *
         * Should this method go in address.h?
         *
         * It calculates the hash taking the uint32_t hash value of the IPv4 or IPv6 address.
         * It works only for InetSocketAddresses (IPv4 version) or Inet6SocketAddresses (IPv6
         * version)
         */
        size_t operator()(const Address& x) const
        {
            if (InetSocketAddress::IsMatchingType(x))
            {
                InetSocketAddress a = InetSocketAddress::ConvertFrom(x);
                return Ipv4AddressHash()(a.GetIpv4());
            }
            else if (Inet6SocketAddress::IsMatchingType(x))
            {
                Inet6SocketAddress a = Inet6SocketAddress::ConvertFrom(x);
                return Ipv6AddressHash()(a.GetIpv6());
            }

            NS_ABORT_MSG("PacketSink: unexpected address type, neither IPv4 nor IPv6");
            return 0; // silence the warnings.
        }
    };

    std::unordered_map<Address, Ptr<Packet>, AddressHash> m_buffer; //!< Buffer for received packets

    // In the case of TCP, each socket accept returns a new socket, so the
    // listening socket is stored separately from the accepted sockets
    Ptr<Socket> m_socket;                //!< Listening socket
    std::list<Ptr<Socket>> m_socketList; //!< the accepted sockets

    Address m_local;      //!< Local address to bind to (address and port)
    uint16_t m_localPort; //!< Local port to bind to
    uint64_t m_totalRx;   //!< Total bytes received
    TypeId m_tid;         //!< Protocol TypeId

    uint64_t counter;

    bool m_enableSeqTsSizeHeader{false}; //!< Enable or disable the export of SeqTsSize header

    /// Traced Callback: received packets, source address.
    TracedCallback<Ptr<const Packet>, const Address&> m_rxTrace;
    /// Callback for tracing the packet Rx events, includes source and destination addresses
    TracedCallback<Ptr<const Packet>, const Address&, const Address&> m_rxTraceWithAddresses;
    /// Callbacks for tracing the packet Rx events, includes source, destination addresses, and
    /// headers
    TracedCallback<Ptr<const Packet>, const Address&, const Address&, const SeqTsSizeHeader&>
        m_rxTraceWithSeqTsSize;
};

}

#endif
