package ipxtunneltest.client.middleman;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.DatagramPacket;

import ipxtunnel.client.PacketInjector;
import ipxtunnel.client.broadcastpackets.BroadcastInjector;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.ServerBoundPacketSender;

import org.junit.Test;

public class MiddlemanTest
{

    @Test
    public void testMiddlemanGuidesPackets() throws IOException
    {
        PacketInjector injector = mock(PacketInjector.class);
        PacketListener receiver = mock(PacketListener.class);
        ServerBoundPacketSender sender = mock(ServerBoundPacketSender.class);
        MiddleMan middleMan = new MiddleMan(sender, injector, receiver);
        
        middleMan.handleOnePacket();
        
        verify(receiver).listen();
        verify(injector).inject(any(DatagramPacket.class));
        verify(sender).send(any(DatagramPacket.class));
    }

}
