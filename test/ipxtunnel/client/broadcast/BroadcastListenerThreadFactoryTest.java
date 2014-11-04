package ipxtunnel.client.broadcast;

import static ipxtunnel.thread.ThreadTest.runOneCycle;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManFactory;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;

import java.io.IOException;
import java.net.MulticastSocket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BroadcastListenerThreadFactoryTest
{
    @InjectMocks
    private BroadcastListenerThreadFactory broadcastListenerThreadFactory;
    
    @Mock
    private ConnectionDetails broadcastConnectionDetails;
    
    @Mock
    private MiddleMan broadcastListener;
    
    @Mock
    private BroadcastHandler broadcastHandler;

    @Mock
    private BroadcastListenerFactory broadcastListenerFactory;
    
    @Mock
    private PacketListener packetListener;

    @Mock
    private BroadcastHandlerFactory broadcastHandlerFactory;

    @Mock
    private MiddleManFactory middleManFactory;
    
    @Mock
    private ConnectionDetails serverConnectionDetails;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldConstructBroadcastListenerThread() throws InterruptedException, IOException 
    {
        when(broadcastListenerFactory.construct(broadcastConnectionDetails)).thenReturn(packetListener);
        when(broadcastHandlerFactory.construct(serverConnectionDetails)).thenReturn(broadcastHandler);
        when(middleManFactory.construct(packetListener, broadcastHandler)).thenReturn(broadcastListener);
        
        MiddleManThread constructedThread = broadcastListenerThreadFactory.construct(serverConnectionDetails, broadcastConnectionDetails);

        runOneCycle(constructedThread);
        
        verify(broadcastListener).handleOnePacket();
    }
}
