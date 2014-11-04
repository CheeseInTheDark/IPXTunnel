package ipxtunnel.client.broadcast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

import java.net.DatagramSocket;
import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BroadcastHandlerFactoryTest
{

    @InjectMocks
    private BroadcastHandlerFactory broadcastHandlerFactory;
    
    @Mock
    private PacketSenderFactory packetSenderFactory;
    
    @Mock
    private DatagramSocket sendingSocket;
    
    @Mock
    private PacketSender packetSender;
    
    @Mock
    private ConnectionDetails destination;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldCreateBroadcastHandler() throws SocketException
    {
        when(packetSenderFactory.construct(destination)).thenReturn(packetSender);
        
        BroadcastHandler handler = broadcastHandlerFactory.construct(destination);
        
        assertThat(handler, is(notNullValue()));
    }
}
