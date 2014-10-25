package ipxtunnel.client.broadcast;

import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.socketwrappers.PacketSender;

import java.net.DatagramPacket;

public class BroadcastHandler implements PacketHandler
{

    private PacketSender sender;
    
    @Override
    public void handle(DatagramPacket packet)
    {
        sender.send(packet);
    }

}
