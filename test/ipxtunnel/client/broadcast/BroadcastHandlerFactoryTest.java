package ipxtunnel.client.broadcast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.net.DatagramSocket;

import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    public void shouldCreateBroadcastHandler()
    {
        when(packetSenderFactory.construct(sendingSocket, destination)).thenReturn(packetSender);
        
        BroadcastHandler handler = broadcastHandlerFactory.construct(destination, sendingSocket);
        
        assertThat(handler, is(notNullValue()));
    }
}
