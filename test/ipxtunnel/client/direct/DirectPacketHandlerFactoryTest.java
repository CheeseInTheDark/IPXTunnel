package ipxtunnel.client.direct;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.net.SocketException;

import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DirectPacketHandlerFactoryTest
{

    private DirectPacketHandlerFactory underTest = new DirectPacketHandlerFactory();
    
    @Mock
    private ConnectionDetails serverConnectionDetails;

    private int receivingPort = 3;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldConstructPacketHandler() throws SocketException
    {
        DirectPacketHandler handler = underTest.construct(serverConnectionDetails, receivingPort);
        
        assertThat(handler, is(notNullValue()));
    }
}
