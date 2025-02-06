#ifndef PRIORITY_TAG_H
#define PRIORITY_TAG_H

#include "ns3/tag.h"
#include "ns3/tag-buffer.h"
#include <stdint.h>

namespace ns3
{
class PriorityTag : public Tag{
public:
    static TypeId GetTypeId(void);
    PriorityTag();
    virtual ~PriorityTag();
    virtual TypeId GetInstanceTypeId(void) const;
    virtual uint32_t GetSerializedSize(void) const;
    virtual void Serialize (TagBuffer i) const;
    virtual void Deserialize (TagBuffer i);
    virtual void Print (std::ostream & os) const;

    void SetPriority(double v);
    double GetPriority();
private:
    double m_priority;
};
}
#endif