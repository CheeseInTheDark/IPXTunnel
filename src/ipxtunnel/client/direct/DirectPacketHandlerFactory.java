package ipxtunnel.client.direct;

import ipxtunnel.client.properties.ConnectionDetails;

public class DirectPacketHandlerFactory
{

    public DirectPacketHandler construct(ConnectionDetails serverConnectionDetails)
    {
        return new DirectPacketHandler();
    }

}
