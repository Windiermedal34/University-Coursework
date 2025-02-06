#include "ns3/log.h"
#include "ns3/trace-source-accessor.h"
#include "MyQueue.h"

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


NS_LOG_COMPONENT_DEFINE("MyQueue");

NS_OBJECT_ENSURE_REGISTERED(MyQueueBase);
NS_OBJECT_TEMPLATE_CLASS_DEFINE(MyQueue, Packet);
NS_OBJECT_TEMPLATE_CLASS_DEFINE(MyQueue, QueueDiscItem);

TypeId
MyQueueBase::GetTypeId()
{
    static TypeId tid = TypeId("ns3::MyQueueBase")
                            .SetParent<Object>()
                            .SetGroupName("Network")
                            .AddTraceSource("PacketsInQueue",
                                            "Number of packets currently stored in the queue",
                                            MakeTraceSourceAccessor(&MyQueueBase::m_nPackets),
                                            "ns3::TracedValueCallback::Uint32")
                            .AddTraceSource("BytesInQueue",
                                            "Number of bytes currently stored in the queue",
                                            MakeTraceSourceAccessor(&MyQueueBase::m_nBytes),
                                            "ns3::TracedValueCallback::Uint32");
    return tid;
}

MyQueueBase::MyQueueBase()
    : m_nBytes(0),
      m_nTotalReceivedBytes(0),
      m_nPackets(0),
      m_nTotalReceivedPackets(0),
      m_nTotalDroppedBytes(0),
      m_nTotalDroppedBytesBeforeEnqueue(0),
      m_nTotalDroppedBytesAfterDequeue(0),
      m_nTotalDroppedPackets(0),
      m_nTotalDroppedPacketsBeforeEnqueue(0),
      m_nTotalDroppedPacketsAfterDequeue(0)
{
    NS_LOG_FUNCTION(this);
    m_maxSize = QueueSize(QueueSizeUnit::PACKETS, std::numeric_limits<uint32_t>::max());
}

MyQueueBase::~MyQueueBase()
{
    NS_LOG_FUNCTION(this);
}

void
MyQueueBase::AppendItemTypeIfNotPresent(std::string& typeId, const std::string& itemType)
{
    if (typeId.back() != '>')
    {
        typeId.append("<" + itemType + ">");
    }
}

bool
MyQueueBase::IsEmpty() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << (m_nPackets.Get() == 0));
    return m_nPackets.Get() == 0;
}

uint32_t
MyQueueBase::GetNPackets() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nPackets);
    return m_nPackets;
}

uint32_t
MyQueueBase::GetNBytes() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC(" returns " << m_nBytes);
    return m_nBytes;
}

QueueSize
MyQueueBase::GetCurrentSize() const
{
    NS_LOG_FUNCTION(this);

    if (m_maxSize.GetUnit() == QueueSizeUnit::PACKETS)
    {
        return QueueSize(QueueSizeUnit::PACKETS, m_nPackets);
    }
    if (m_maxSize.GetUnit() == QueueSizeUnit::BYTES)
    {
        return QueueSize(QueueSizeUnit::BYTES, m_nBytes);
    }
    NS_ABORT_MSG("Unknown queue size unit");
}

uint32_t
MyQueueBase::GetTotalReceivedBytes() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nTotalReceivedBytes);
    return m_nTotalReceivedBytes;
}

uint32_t
MyQueueBase::GetTotalReceivedPackets() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nTotalReceivedPackets);
    return m_nTotalReceivedPackets;
}

uint32_t
MyQueueBase::GetTotalDroppedBytes() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nTotalDroppedBytes);
    return m_nTotalDroppedBytes;
}

uint32_t
MyQueueBase::GetTotalDroppedBytesBeforeEnqueue() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nTotalDroppedBytesBeforeEnqueue);
    return m_nTotalDroppedBytesBeforeEnqueue;
}

uint32_t
MyQueueBase::GetTotalDroppedBytesAfterDequeue() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nTotalDroppedBytesAfterDequeue);
    return m_nTotalDroppedBytesAfterDequeue;
}

uint32_t
MyQueueBase::GetTotalDroppedPackets() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nTotalDroppedPackets);
    return m_nTotalDroppedPackets;
}

uint32_t
MyQueueBase::GetTotalDroppedPacketsBeforeEnqueue() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nTotalDroppedPacketsBeforeEnqueue);
    return m_nTotalDroppedPacketsBeforeEnqueue;
}

uint32_t
MyQueueBase::GetTotalDroppedPacketsAfterDequeue() const
{
    NS_LOG_FUNCTION(this);
    NS_LOG_LOGIC("returns " << m_nTotalDroppedPacketsAfterDequeue);
    return m_nTotalDroppedPacketsAfterDequeue;
}

void
MyQueueBase::ResetStatistics()
{
    NS_LOG_FUNCTION(this);
    m_nTotalReceivedBytes = 0;
    m_nTotalReceivedPackets = 0;
    m_nTotalDroppedBytes = 0;
    m_nTotalDroppedBytesBeforeEnqueue = 0;
    m_nTotalDroppedBytesAfterDequeue = 0;
    m_nTotalDroppedPackets = 0;
    m_nTotalDroppedPacketsBeforeEnqueue = 0;
    m_nTotalDroppedPacketsAfterDequeue = 0;
}

void
MyQueueBase::SetMaxSize(QueueSize size)
{
    NS_LOG_FUNCTION(this << size);

    // do nothing if the size is null
    if (!size.GetValue())
    {
        return;
    }

    m_maxSize = size;

    NS_ABORT_MSG_IF(size < GetCurrentSize(),
                    "The new maximum queue size cannot be less than the current size");
}

QueueSize
MyQueueBase::GetMaxSize() const
{
    NS_LOG_FUNCTION(this);
    return m_maxSize;
}

bool
MyQueueBase::WouldOverflow(uint32_t nPackets, uint32_t nBytes) const
{
    if (m_maxSize.GetUnit() == QueueSizeUnit::PACKETS)
    {
        return (m_nPackets + nPackets > m_maxSize.GetValue());
    }
    else
    {
        return (m_nBytes + nBytes > m_maxSize.GetValue());
    }
}

} 