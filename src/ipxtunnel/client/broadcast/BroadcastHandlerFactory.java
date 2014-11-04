package ipxtunnel.client.broadcast;

import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

import java.net.SocketException;

public class BroadcastHandlerFactory
{
    private PacketSenderFactory packetSenderFactory = new PacketSenderFactory();

    public BroadcastHandler construct(ConnectionDetails destination) throws SocketException
    {
        PacketSender sender = packetSenderFactory.construct(destination);
        return new BroadcastHandler(sender);
    }

}
