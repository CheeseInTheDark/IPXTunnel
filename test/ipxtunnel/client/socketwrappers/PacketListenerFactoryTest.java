package ipxtunnel.client.socketwrappers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.DatagramSocket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PacketListenerFactoryTest
{

    @Mock
    private DatagramSocket socket;
    
    private PacketListenerFactory underTest = new PacketListenerFactory();;
    
    @Before
    public void setup() throws IOException
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldCreatePacketListener() throws IOException
    {
        PacketListener result = underTest.construct(socket);
        
        assertThat(result, is(notNullValue()));
    }
}
