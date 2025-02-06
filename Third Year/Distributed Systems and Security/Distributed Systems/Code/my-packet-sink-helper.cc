#include "my-packet-sink-helper.h"

#include "ns3/inet-socket-address.h"
#include "ns3/names.h"
#include "ns3/string.h"

namespace ns3
{

MyPacketSinkHelper::MyPacketSinkHelper(std::string protocol, Address address)
{
    m_factory.SetTypeId("ns3::MyPacketSink");
    m_factory.Set("Protocol", StringValue(protocol));
    m_factory.Set("Local", AddressValue(address));
}

void
MyPacketSinkHelper::SetAttribute(std::string name, const AttributeValue& value)
{
    m_factory.Set(name, value);
}

ApplicationContainer
MyPacketSinkHelper::Install(Ptr<Node> node) const
{
    return ApplicationContainer(InstallPriv(node));
}

ApplicationContainer
MyPacketSinkHelper::Install(std::string nodeName) const
{
    Ptr<Node> node = Names::Find<Node>(nodeName);
    return ApplicationContainer(InstallPriv(node));
}

ApplicationContainer
MyPacketSinkHelper::Install(NodeContainer c) const
{
    ApplicationContainer apps;
    for (NodeContainer::Iterator i = c.Begin(); i != c.End(); ++i)
    {
        apps.Add(InstallPriv(*i));
    }

    return apps;
}

Ptr<Application>
MyPacketSinkHelper::InstallPriv(Ptr<Node> node) const
{
    Ptr<Application> app = m_factory.Create<Application>();
    node->AddApplication(app);

    return app;
}

}