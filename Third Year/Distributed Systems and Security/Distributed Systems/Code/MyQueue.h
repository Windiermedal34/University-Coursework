#ifndef MY_QUEUE_H
#define MY_QUEUE_H

#include "ns3/log.h"
#include "ns3/object.h"
#include "ns3/packet.h"
#include "ns3/queue-fwd.h"
#include "ns3/queue-item.h"
#include "ns3/queue-size.h"
#include "ns3/traced-callback.h"
#include "ns3/traced-value.h"

#include <sstream>
#include <string>
#include <type_traits>

namespace ns3
{

class MyQueueBase : public Object
{
public:
    static TypeId GetTypeId();

    MyQueueBase();
    ~MyQueueBase() override;

    static void AppendItemTypeIfNotPresent(std::string& typeId, const std::string& itemType);

    bool IsEmpty() const;

    uint32_t GetNPackets() const;

    uint32_t GetNBytes() const;

    QueueSize GetCurrentSize() const;

    uint32_t GetTotalReceivedBytes() const;

    uint32_t GetTotalReceivedPackets() const;

    uint32_t GetTotalDroppedBytes() const;

    uint32_t GetTotalDroppedBytesBeforeEnqueue() const;

    uint32_t GetTotalDroppedBytesAfterDequeue() const;

    uint32_t GetTotalDroppedPackets() const;

    uint32_t GetTotalDroppedPacketsBeforeEnqueue() const;

    uint32_t GetTotalDroppedPacketsAfterDequeue() const;

    void ResetStatistics();

    void SetMaxSize(QueueSize size);

    QueueSize GetMaxSize() const;

    bool WouldOverflow(uint32_t nPackets, uint32_t nBytes) const;

#if 0
  // average calculation requires keeping around
  // a buffer with the date of arrival of past received packets
  // which are within the average window
  // so, it is quite costly to do it all the time.
  // Hence, it is disabled by default and must be explicitly
  // enabled with this method which specifies the size
  // of the average window in time units.
  void EnableRunningAverage(Time averageWindow);
  void DisableRunningAverage();
  // average
  double GetQueueSizeAverage();
  double GetReceivedBytesPerSecondAverage();
  double GetReceivedPacketsPerSecondAverage();
  double GetDroppedBytesPerSecondAverage();
  double GetDroppedPacketsPerSecondAverage();
  // variance
  double GetQueueSizeVariance();
  double GetReceivedBytesPerSecondVariance();
  double GetReceivedPacketsPerSecondVariance();
  double GetDroppedBytesPerSecondVariance();
  double GetDroppedPacketsPerSecondVariance();
#endif

  protected:
    TracedValue<uint32_t> m_nBytes;               //!< Number of bytes in the queue
    uint32_t m_nTotalReceivedBytes;               //!< Total received bytes
    TracedValue<uint32_t> m_nPackets;             //!< Number of packets in the queue
    uint32_t m_nTotalReceivedPackets;             //!< Total received packets
    uint32_t m_nTotalDroppedBytes;                //!< Total dropped bytes
    uint32_t m_nTotalDroppedBytesBeforeEnqueue;   //!< Total dropped bytes before enqueue
    uint32_t m_nTotalDroppedBytesAfterDequeue;    //!< Total dropped bytes after dequeue
    uint32_t m_nTotalDroppedPackets;              //!< Total dropped packets
    uint32_t m_nTotalDroppedPacketsBeforeEnqueue; //!< Total dropped packets before enqueue
    uint32_t m_nTotalDroppedPacketsAfterDequeue;  //!< Total dropped packets after dequeue

    QueueSize m_maxSize; //!< max queue size
};

template <typename Item>
class MyQueue : public MyQueueBase
{
public:
    static TypeId GetTypeId();

    MyQueue();
    ~MyQueue() override;

    virtual bool Enqueue(Ptr<Item> item);

    virtual Ptr<Item> Dequeue();

    virtual Ptr<Item> Remove();

    virtual Ptr<const Item> Peek() const;

    void Flush();

    typedef Item ItemType;
    
    bool DoEnqueue(Ptr<Item> item);

    void ClearQueue();

  protected:
    /// Const iterator.
    typedef typename std::list<Ptr<Item>>::const_iterator ConstIterator;
    /// Iterator.
    typedef typename std::list<Ptr<Item>>::iterator Iterator;

    const std::list<Ptr<Item>>& GetContainer() const;

    

    bool DoEnqueue(ConstIterator pos, Ptr<Item> item);

    bool DoEnqueue(ConstIterator pos, Ptr<Item> item, Iterator& ret);

    Ptr<Item> DoDequeue(ConstIterator pos);

    Ptr<Item> DoRemove(ConstIterator pos);

    Ptr<const Item> DoPeek(ConstIterator pos) const;

    void DropBeforeEnqueue(Ptr<Item> item);

    void DropAfterDequeue(Ptr<Item> item);

    void DoDispose() override;

private:
    template <class, class = void>
    struct MakeGetItem
    {
        static Ptr<Item> GetItem(const std::list<Ptr<Item>>&, const ConstIterator it)
        {
            return *it;
        }
    };

    template <class T>
    struct MakeGetItem<
        T,
        std::void_t<decltype(std::declval<T>().GetItem(std::declval<ConstIterator>()))>>
    {
        static Ptr<Item> GetItem(const std::list<Ptr<Item>>& container, const ConstIterator it)
        {
            return container.GetItem(it);
        }
    };

    std::list<Ptr<Item>> m_packets;     //!< the items in the queue
    NS_LOG_TEMPLATE_DECLARE; //!< the log component

    /// Traced callback: fired when a packet is enqueued
    TracedCallback<Ptr<const Item>> m_traceEnqueue;
    /// Traced callback: fired when a packet is dequeued
    TracedCallback<Ptr<const Item>> m_traceDequeue;
    /// Traced callback: fired when a packet is dropped
    TracedCallback<Ptr<const Item>> m_traceDrop;
    /// Traced callback: fired when a packet is dropped before enqueue
    TracedCallback<Ptr<const Item>> m_traceDropBeforeEnqueue;
    /// Traced callback: fired when a packet is dropped after dequeue
    TracedCallback<Ptr<const Item>> m_traceDropAfterDequeue;
};

/**
 * Implementation of the templates declared above.
 */

template <typename Item>
TypeId
MyQueue<Item>::GetTypeId()
{
    std::string name = GetTemplateClassName<MyQueue<Item>>();
    auto startPos = name.find('<') + 1;
    auto endPos = name.find_first_of(",>", startPos);
    std::string tcbName = "ns3::" + name.substr(startPos, endPos - startPos) + "::TracedCallback";

    static TypeId tid =
        TypeId(name)
            .SetParent<MyQueueBase>()
            .SetGroupName("Network")
            .AddAttribute("MaxSize",
                          "The max queue size",
                          QueueSizeValue(QueueSize("100p")),
                          MakeQueueSizeAccessor(&MyQueueBase::SetMaxSize, &MyQueueBase::GetMaxSize),
                          MakeQueueSizeChecker())
            .AddTraceSource("Enqueue",
                            "Enqueue a packet in the queue.",
                            MakeTraceSourceAccessor(&MyQueue<Item>::m_traceEnqueue),
                            tcbName)
            .AddTraceSource("Dequeue",
                            "Dequeue a packet from the queue.",
                            MakeTraceSourceAccessor(&MyQueue<Item>::m_traceDequeue),
                            tcbName)
            .AddTraceSource("Drop",
                            "Drop a packet (for whatever reason).",
                            MakeTraceSourceAccessor(&MyQueue<Item>::m_traceDrop),
                            tcbName)
            .AddTraceSource(
                "DropBeforeEnqueue",
                "Drop a packet before enqueue.",
                MakeTraceSourceAccessor(&MyQueue<Item>::m_traceDropBeforeEnqueue),
                tcbName)
            .AddTraceSource(
                "DropAfterDequeue",
                "Drop a packet after dequeue.",
                MakeTraceSourceAccessor(&MyQueue<Item>::m_traceDropAfterDequeue),
                tcbName);
    return tid;
}

template <typename Item>
MyQueue<Item>::MyQueue()
    : NS_LOG_TEMPLATE_DEFINE("Queue")
{
}

template <typename Item>
MyQueue<Item>::~MyQueue()
{
}

template <typename Item>
const std::list<Ptr<Item>>&
MyQueue<Item>::GetContainer() const
{
    return m_packets;
}

template <typename Item>
void
MyQueue<Item>::ClearQueue()
{
    m_packets.clear();
}

template <typename Item>
bool
MyQueue<Item>::Enqueue(Ptr<Item> item)
{
    NS_LOG_FUNCTION(this << item);
    return DoEnqueue(GetContainer().end(), item);
    // return DoEnqueue(item);
}

template <typename Item>
bool
MyQueue<Item>:: DoEnqueue(Ptr<Item> item)
{
    NS_LOG_FUNCTION(this << item);

    if (GetCurrentSize() >= GetMaxSize())
    {
        NS_LOG_LOGIC("Queue full -- dropping pkt");
        DropBeforeEnqueue(item);
        return false;
    }

    m_packets.push_back(item);
    // uint32_t size = item.GetSize();
    // m_nBytes += size;
    // m_nTotalReceivedBytes += size;

    m_nPackets++;
    m_nTotalReceivedPackets++;

    NS_LOG_LOGIC("m_traceEnqueue (p)");
    m_traceEnqueue(item);
    return true;
}

template <typename Item>
bool
MyQueue<Item>::DoEnqueue(ConstIterator pos, Ptr<Item> item)
{
    Iterator ret;
    return DoEnqueue(pos, item, ret);
}

template <typename Item>
bool
MyQueue<Item>::DoEnqueue(ConstIterator pos, Ptr<Item> item, Iterator& ret)
{
    NS_LOG_FUNCTION(this << item);

    if (GetCurrentSize() + item > GetMaxSize())
    {
        NS_LOG_LOGIC("Queue full -- dropping pkt");
        DropBeforeEnqueue(item);
        return false;
    }

    ret = m_packets.insert(pos, item);

    // uint32_t size = item->GetSize();
    uint32_t size = 512;
    m_nBytes += size;
    m_nTotalReceivedBytes += size;

    m_nPackets++;
    m_nTotalReceivedPackets++;

    NS_LOG_LOGIC("m_traceEnqueue (p)");
    m_traceEnqueue(item);

    return true;
}

template <typename Item>
Ptr<Item>
MyQueue<Item>::Dequeue()
{
    NS_LOG_FUNCTION(this);
    
    Ptr<Item> item = DoDequeue(GetContainer().begin());

    NS_LOG_LOGIC("Popped " << item);

    return item;
}

template <typename Item>
Ptr<Item>
MyQueue<Item>::DoDequeue(ConstIterator pos)
{
    NS_LOG_FUNCTION(this);

    if (m_nPackets.Get() == 0)
    {
        NS_LOG_LOGIC("Queue empty");
        return nullptr;
    }

    Ptr<Item> item = MakeGetItem<std::list<Ptr<Item>>>::GetItem(m_packets, pos);

    if (item)
    {
        m_packets.erase(pos);
        NS_ASSERT(m_nBytes.Get() >= item->GetSize());
        NS_ASSERT(m_nPackets.Get() > 0);

        m_nBytes -= item->GetSize();
        m_nPackets--;

        NS_LOG_LOGIC("m_traceDequeue (p)");
        m_traceDequeue(item);
    }
    return item;
}

template <typename Item>
Ptr<Item>
MyQueue<Item>::Remove()
{
    NS_LOG_FUNCTION(this);

    Ptr<Item> item = DoRemove(GetContainer().begin());

    NS_LOG_LOGIC("Removed " << item);

    return item;
}

template <typename Item>
Ptr<Item>
MyQueue<Item>::DoRemove(ConstIterator pos)
{
    NS_LOG_FUNCTION(this);

    if (m_nPackets.Get() == 0)
    {
        NS_LOG_LOGIC("Queue empty");
        return nullptr;
    }

    Ptr<Item> item = MakeGetItem<std::list<Ptr<Item>>>::GetItem(m_packets, pos);

    if (item)
    {
        m_packets.erase(pos);
        NS_ASSERT(m_nBytes.Get() >= item->GetSize());
        NS_ASSERT(m_nPackets.Get() > 0);

        m_nBytes -= item->GetSize();
        m_nPackets--;

        // packets are first dequeued and then dropped
        NS_LOG_LOGIC("m_traceDequeue (p)");
        m_traceDequeue(item);

        DropAfterDequeue(item);
    }
    return item;
}

template <typename Item>
void
MyQueue<Item>::Flush()
{
    NS_LOG_FUNCTION(this);
    while (!IsEmpty())
    {
        Remove();
    }
}

template <typename Item>
void
MyQueue<Item>::DoDispose()
{
    NS_LOG_FUNCTION(this);
    m_packets.clear();
    Object::DoDispose();
}

template <typename Item>
Ptr<const Item>
MyQueue<Item>::Peek() const
{
    NS_LOG_FUNCTION(this);

    return DoPeek(GetContainer().begin());
}

template <typename Item>
Ptr<const Item>
MyQueue<Item>::DoPeek(ConstIterator pos) const
{
    NS_LOG_FUNCTION(this);

    if (m_nPackets.Get() == 0)
    {
        NS_LOG_LOGIC("Queue empty");
        return nullptr;
    }

    return MakeGetItem<std::list<Ptr<Item>>>::GetItem(m_packets, pos);
}

template <typename Item>
void
MyQueue<Item>::DropBeforeEnqueue(Ptr<Item> item)
{
    NS_LOG_FUNCTION(this << item);

    m_nTotalDroppedPackets++;
    m_nTotalDroppedPacketsBeforeEnqueue++;
    m_nTotalDroppedBytes += item->GetSize();
    m_nTotalDroppedBytesBeforeEnqueue += item->GetSize();

    NS_LOG_LOGIC("m_traceDropBeforeEnqueue (p)");
    m_traceDrop(item);
    m_traceDropBeforeEnqueue(item);
}

template <typename Item>
void
MyQueue<Item>::DropAfterDequeue(Ptr<Item> item)
{
    NS_LOG_FUNCTION(this << item);

    m_nTotalDroppedPackets++;
    m_nTotalDroppedPacketsAfterDequeue++;
    m_nTotalDroppedBytes += item->GetSize();
    m_nTotalDroppedBytesAfterDequeue += item->GetSize();

    NS_LOG_LOGIC("m_traceDropAfterDequeue (p)");
    m_traceDrop(item);
    m_traceDropAfterDequeue(item);
}

// The following explicit template instantiation declarations prevent all the
// translation units including this header file to implicitly instantiate the
// Queue<Packet> class and the Queue<QueueDiscItem> class. The unique instances
// of these classes are explicitly created through the macros
// NS_OBJECT_TEMPLATE_CLASS_DEFINE (Queue,Packet) and
// NS_OBJECT_TEMPLATE_CLASS_DEFINE (Queue,QueueDiscItem), which are included in queue.cc
extern template class MyQueue<Packet>;
extern template class MyQueue<QueueDiscItem>;

} // namespace ns3

#endif /* QUEUE_H */
