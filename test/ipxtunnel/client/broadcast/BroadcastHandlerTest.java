package ipxtunnel.client.broadcast;

import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;

import ipxtunnel.client.socketwrappers.PacketSender;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class BroadcastHandlerTest
{

    @InjectMocks
    private BroadcastHandler broadcastHandler;
    
    @Mock
    private PacketSender sender;
    
    private DatagramPacket packet;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        
        packet = new DatagramPacket(new byte[0], 0);
    }
    
    @Test
    public void shouldRoutePacketToServer() throws IOException
    {
        broadcastHandler.handle(packet);
        
        verify(sender).send(packet);
    }
}
