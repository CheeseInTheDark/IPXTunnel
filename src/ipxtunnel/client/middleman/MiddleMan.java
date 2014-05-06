package ipxtunnel.client.middleman;

import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketSender;

import java.io.IOException;
import java.net.DatagramPacket;

public class MiddleMan
{
    PacketModifier modifier;
    PacketSender sender;
    PacketListener receiver;
    
    public MiddleMan(PacketSender sender, PacketModifier modifier, PacketListener receiver)
    {
        this.receiver = receiver;
        this.modifier = modifier;
        this.sender = sender;
    }

    public void handleOnePacket() throws IOException
    {
        DatagramPacket packet = receiver.listen();
        modifier.modify(packet);
        sender.send(packet);
    }
}
