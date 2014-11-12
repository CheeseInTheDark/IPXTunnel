package ipxtunnel.client.direct;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;

import ipxtunnel.client.injectors.DirectPacketInjector;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.socketwrappers.PacketSender;

public class DirectPacketHandler implements PacketHandler
{

    private PacketSender sender;
    private DirectPacketInjector injector;
    
    public DirectPacketHandler(PacketSender sender, DirectPacketInjector injector)
    {
        this.sender = sender;
        this.injector = injector;
    }

    @Override
    public void handle(DatagramPacket packet)
    {
        injector.inject(packet);
        try
        {
            sender.send(packet);
        }
        catch (IOException e)
        {
        }
    }

}
