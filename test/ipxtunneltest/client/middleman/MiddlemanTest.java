package ipxtunneltest.client.middleman;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketSender;

import java.io.IOException;
import java.net.DatagramPacket;

import org.junit.Test;

public class MiddlemanTest
{

    @Test
    public void testMiddlemanGuidesPackets() throws IOException
    {
        PacketHandler injector = mock(PacketHandler.class);
        PacketListener receiver = mock(PacketListener.class);
        PacketSender sender = mock(PacketSender.class);
        MiddleMan middleMan = new MiddleMan(sender, injector, receiver);
        
        middleMan.handleOnePacket();
        
        verify(receiver).listen();
        verify(injector).modify(any(DatagramPacket.class));
        verify(sender).send(any(DatagramPacket.class));
    }
}
