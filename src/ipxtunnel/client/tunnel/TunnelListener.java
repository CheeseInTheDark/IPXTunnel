package ipxtunnel.client.tunnel;

import java.io.IOException;
import java.net.DatagramPacket;

import ipxtunnel.client.injectors.PacketUnwrapper;
import ipxtunnel.client.socketwrappers.FakeNodeSender;
import ipxtunnel.client.socketwrappers.PacketListener;

public class TunnelListener
{
    PacketListener receiver;
    PacketUnwrapper unwrapper;
    
    public TunnelListener(PacketListener receiver, PacketUnwrapper unwrapper,
            FakeNodeSender sender)
    {
        this.receiver = receiver;
        this.unwrapper = unwrapper;
    }

    public void handleOnePacket() throws IOException
    {
        DatagramPacket packet = receiver.listen();
        unwrapper.unwrap(packet);
    }

}
