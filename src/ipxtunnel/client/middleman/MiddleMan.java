package ipxtunnel.client.middleman;

import ipxtunnel.client.PacketInjector;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.ServerBoundPacketSender;

import java.io.IOException;
import java.net.DatagramPacket;

public class MiddleMan
{
    PacketInjector injector;
    ServerBoundPacketSender sender;
    PacketListener receiver;
    
    public MiddleMan(ServerBoundPacketSender sender, PacketInjector injector, PacketListener receiver)
    {
        this.receiver = receiver;
        this.injector = injector;
        this.sender = sender;
    }

    public void handleOnePacket() throws IOException
    {
        DatagramPacket packet = receiver.listen();
        injector.inject(packet);
        sender.send(packet);
    }
}
