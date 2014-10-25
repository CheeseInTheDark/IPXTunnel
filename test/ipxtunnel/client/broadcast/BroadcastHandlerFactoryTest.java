package ipxtunnel.client.broadcast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.net.DatagramSocket;

import ipxtunnel.client.socketwrappers.PacketListener;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BroadcastHandlerFactoryTest
{

    private BroadcastHandlerFactory broadcastHandlerFactory;
    
    @Mock
    private DatagramSocket sendingSocket;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        broadcastHandlerFactory = new BroadcastHandlerFactory();
    }
    
    @Test
    public void shouldCreateBroadcastHandler()
    {
        BroadcastHandler handler = broadcastHandlerFactory.construct(sendingSocket);
        
        assertThat(handler, is(notNullValue()));
    }
}
