package ipxtunnel.client.broadcast;

import ipxtunnel.client.injectors.BroadcastInjector;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.socketwrappers.PacketSender;

import java.io.IOException;
import java.net.DatagramPacket;

public class BroadcastHandler implements PacketHandler
{

    private PacketSender sender;
    private BroadcastInjector injector = new BroadcastInjector();
    
    public BroadcastHandler(PacketSender sender)
    {
        this.sender = sender;
    }
    
    @Override
    public void handle(DatagramPacket packet)
    {
        try
        {
            injector.inject(packet);
            sender.send(packet);
        }
        catch (IOException e) {}
    }

}
