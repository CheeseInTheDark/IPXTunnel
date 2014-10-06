package ipxtunnel.client.tunnel;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.DatagramSocket;

import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class TunnelListenerThreadFactoryTest
{
    @Mock
    private MiddleMan tunnelListener;
    
    @Mock
    private TunnelListenerFactory tunnelListenerFactory;
    
    @Mock
    private NodeDelegates nodeDelegates;
    
    @Mock
    private PacketListener packetListener;
    
    @Mock
    private DatagramSocket receivesFromServer;
    
    @Mock
    private PacketListenerFactory packetListenerFactory;
    
    @Mock
    private TunnelHandlerFactory tunnelHandlerFactory;
    
    @Mock
    private TunnelHandler tunnelHandler;
    
    @InjectMocks
    TunnelListenerThreadFactory underTest;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldConstructTunnelListenerThread() throws InterruptedException, IOException
    {
        when(packetListenerFactory.construct(receivesFromServer)).thenReturn(packetListener);
        when(tunnelHandlerFactory.construct(nodeDelegates)).thenReturn(tunnelHandler);
        when(tunnelListenerFactory.construct(packetListener, tunnelHandler)).thenReturn(tunnelListener);
        
        MiddleManThread constructedThread = underTest.construct(receivesFromServer, nodeDelegates);
        
        runThreadThroughOneCycle(constructedThread);
        
        verify(tunnelListener).handleOnePacket();
    }
    
    public void runThreadThroughOneCycle(MiddleManThread thread) throws InterruptedException
    {
        thread.start();
        thread.interrupt();
        thread.waitForDeath();
    }
}
