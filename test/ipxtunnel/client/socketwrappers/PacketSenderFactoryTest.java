package ipxtunnel.client.socketwrappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import ipxtunnel.client.properties.ConnectionDetails;

import java.net.DatagramSocket;
import java.net.SocketException;

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
    private ConnectionDetails destination;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldCreatePacketSender() throws SocketException
    {
        PacketSender packetSender = packetSenderFactory.construct(destination);
        
        assertThat(packetSender, is(notNullValue()));
    }
}
