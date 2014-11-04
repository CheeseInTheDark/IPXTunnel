package ipxtunnel.client.socketwrappers;

import ipxtunnel.client.properties.ConnectionDetails;

import java.net.SocketException;

public class PacketSenderFactory
{

    public PacketSender construct(ConnectionDetails destination) throws SocketException
    {
        return new PacketSender(destination.getAddress(), destination.getPort());
    }

}
