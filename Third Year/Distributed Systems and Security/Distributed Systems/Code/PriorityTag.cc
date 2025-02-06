#include "PriorityTag.h"

namespace ns3{

NS_LOG_COMPONENT_DEFINE("PriorityTag");
NS_OBJECT_ENSURE_REGISTERED (PriorityTag);

PriorityTag::PriorityTag(){
    m_priority = 0;
}

PriorityTag::~PriorityTag(){
}

TypeId
PriorityTag::GetTypeId(void)
{
    static TypeId tid = TypeId("ns3::PriorityTag")
        .SetParent<Tag>();
    return tid;
}

TypeId
PriorityTag::GetInstanceTypeId(void) const
{
    return GetTypeId();
}

uint32_t
PriorityTag::GetSerializedSize (void) const
{
    return sizeof(double);
}

void
PriorityTag::Serialize (TagBuffer i) const{
    i.WriteDouble (m_priority);
}

void
PriorityTag::Deserialize (TagBuffer i)
{
    m_priority = i.ReadU32();
}

void
PriorityTag::Print (std::ostream &os) const
{
    os << "PositionTag=" << m_priority;
}

void
PriorityTag::SetPriority(double v)
{
    m_priority = v;
}
}