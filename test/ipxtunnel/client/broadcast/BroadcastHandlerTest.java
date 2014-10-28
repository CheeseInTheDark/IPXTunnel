package ipxtunnel.client.broadcast;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;

import ipxtunnel.client.injectors.BroadcastInjector;
import ipxtunnel.client.socketwrappers.PacketSender;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class BroadcastHandlerTest
{

    @InjectMocks
    private BroadcastHandler broadcastHandler = new BroadcastHandler(null);
    
    @Mock
    private BroadcastInjector broadcastInjector;
    
    @Mock
    private PacketSender sender;
    
    private DatagramPacket packet;
    
    private InOrder inOrder;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        
        packet = new DatagramPacket(new byte[0], 0);
        inOrder = inOrder(broadcastInjector, sender);
    }
    
    @Test
    public void shouldRoutePacketToServer() throws IOException
    {
        broadcastHandler.handle(packet);
        
        inOrder.verify(broadcastInjector).inject(packet);
        inOrder.verify(sender).send(packet);
    }
}
