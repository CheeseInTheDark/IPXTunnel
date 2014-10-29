package ipxtunnel.client.broadcast;

import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

import java.net.DatagramSocket;

public class BroadcastHandlerFactory
{
    private PacketSenderFactory packetSenderFactory = new PacketSenderFactory();

    public BroadcastHandler construct(ConnectionDetails connectionDetails, DatagramSocket sendsToServer)
    {
        PacketSender sender = packetSenderFactory.construct(sendsToServer, connectionDetails);
        return new BroadcastHandler(sender);
    }

}
