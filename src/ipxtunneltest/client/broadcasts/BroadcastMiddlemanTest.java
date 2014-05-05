package ipxtunneltest.client.broadcasts;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.DatagramPacket;

import ipxtunnel.client.broadcasts.BroadcastInjector;
import ipxtunnel.client.broadcasts.BroadcastMiddleMan;
import ipxtunnel.client.broadcasts.BroadcastReceiver;
import ipxtunnel.client.broadcasts.BroadcastToServerSender;

import org.junit.Test;

public class BroadcastMiddlemanTest
{

    @Test
    public void testMiddlemanGuidesPackets() throws IOException
    {
        BroadcastInjector injector = mock(BroadcastInjector.class);
        BroadcastReceiver receiver = mock(BroadcastReceiver.class);
        BroadcastToServerSender sender = mock(BroadcastToServerSender.class);
        BroadcastMiddleMan middleMan = new BroadcastMiddleMan(sender, injector, receiver);
        
        middleMan.handleOnePacket();
        
        verify(receiver).listen();
        verify(injector).inject(any(DatagramPacket.class));
        verify(sender).send(any(DatagramPacket.class));
    }

}
