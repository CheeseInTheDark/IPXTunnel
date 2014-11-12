package ipxtunnel.client.direct;

import java.net.SocketException;

import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

public class DirectPacketHandlerFactory
{
    private PacketSenderFactory packetSenderFactory = new PacketSenderFactory();
    
    public DirectPacketHandler construct(ConnectionDetails serverConnectionDetails) throws SocketException
    {
        PacketSender sender = packetSenderFactory.construct(serverConnectionDetails);
        return new DirectPacketHandler(sender);
    }

}
