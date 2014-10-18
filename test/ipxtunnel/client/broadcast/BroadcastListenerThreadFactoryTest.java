package ipxtunnel.client.broadcast;

import ipxtunnel.client.middleman.MiddleManThread;

import java.net.DatagramSocket;
import java.net.MulticastSocket;

import org.junit.Test;
import org.mockito.Mock;


public class BroadcastListenerThreadFactoryTest
{
    private BroadcastListenerThreadFactory broadcastListenerThreadFactory = new BroadcastListenerThreadFactory();
    
    @Mock
    private DatagramSocket sendsToServer;
    
    @Mock
    private MulticastSocket receivesBroadcasts;
    
    @Test
    public void shouldConstructBroadcastListenerThread() 
    {
        MiddleManThread thread = broadcastListenerThreadFactory.construct(sendsToServer, receivesBroadcasts);
        
    }
}
