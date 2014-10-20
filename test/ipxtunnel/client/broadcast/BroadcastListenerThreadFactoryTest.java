package ipxtunnel.client.broadcast;

import static ipxtunnel.thread.ThreadTest.runOneCycle;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;
import ipxtunnel.thread.ThreadTest;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class BroadcastListenerThreadFactoryTest
{
    @InjectMocks
    private BroadcastListenerThreadFactory broadcastListenerThreadFactory;
    
    @Mock
    private DatagramSocket sendsToServer;
    
    @Mock
    private MulticastSocket receivesBroadcasts;
    
    @Mock
    private MiddleMan broadcastListener;
    
    @Mock
    private BroadcastHandler broadcastHandler;

    @Mock
    private PacketListenerFactory packetListenerFactory;
    
    @Mock
    private PacketListener packetListener;

    @Mock
    private BroadcastHandlerFactory broadcastHandlerFactory;

    @Mock
    private BroadcastListenerFactory broadcastListenerFactory;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldConstructBroadcastListenerThread() throws InterruptedException, IOException 
    {
        when(packetListenerFactory.construct(receivesBroadcasts)).thenReturn(packetListener);
        when(broadcastHandlerFactory.construct(sendsToServer)).thenReturn(broadcastHandler);
        when(broadcastListenerFactory.construct(packetListener, broadcastHandler)).thenReturn(broadcastListener);
        
        MiddleManThread constructedThread = broadcastListenerThreadFactory.construct(sendsToServer, receivesBroadcasts);

        runOneCycle(constructedThread);
        
        verify(broadcastListener).handleOnePacket();
    }
}
