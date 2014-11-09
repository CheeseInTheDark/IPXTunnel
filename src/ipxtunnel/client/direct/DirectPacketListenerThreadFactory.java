package ipxtunnel.client.direct;

import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.properties.ConnectionDetails;

import java.net.DatagramSocket;

public class DirectPacketListenerThreadFactory
{

    public MiddleManThread construct(DatagramSocket receivingSocket, ConnectionDetails serverConnectionDetails)
    {
        return null;
    }

}
