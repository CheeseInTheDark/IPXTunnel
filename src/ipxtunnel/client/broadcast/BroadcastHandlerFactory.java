package ipxtunnel.client.broadcast;

import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

import java.net.DatagramSocket;

public class BroadcastHandlerFactory
{
    private PacketSenderFactory packetSenderFactory = new PacketSenderFactory();
    
    public BroadcastHandler construct(DatagramSocket sendsToServer)
    {
        PacketSender sender = packetSenderFactory.construct(sendsToServer);
        return new BroadcastHandler(sender);
    }

}
