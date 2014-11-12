package ipxtunnel.client.direct;

import static org.mockito.Mockito.verify;
import ipxtunnel.client.injectors.DirectPacketInjector;
import ipxtunnel.client.socketwrappers.PacketSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DirectPacketHandlerTest
{
    @InjectMocks
    private DirectPacketHandler underTest;
    
    private DatagramPacket packet;
    
    @Mock
    private DirectPacketInjector injector; 
    
    @Mock
    private PacketSender sender;
    
    private InOrder inOrder; 
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        packet = new DatagramPacket(new byte[0], 0);
        inOrder = Mockito.inOrder(injector, sender);
    }
    
    @Test
    public void shouldAddMetaDataAndSendToServer() throws IOException
    {
        underTest.handle(packet);
        
        inOrder.verify(sender).send(packet);
    }
}
