package ipxtunnel.client.socketwrappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class PacketSenderFactoryTest
{
    private PacketSenderFactory packetSenderFactory = new PacketSenderFactory();
    
    @Mock
    private DatagramSocket sendingSocket;
    
    @Mock
    private InetAddress destinationAddress;
    
    private int destinationPort = 123;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldCreatePacketSender()
    {
        PacketSender packetSender = packetSenderFactory.construct(sendingSocket, destinationAddress, destinationPort);
        
        assertThat(packetSender, is(notNullValue()));
    }
}
