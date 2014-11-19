package ipxtunnel.client.direct;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import ipxtunnel.client.properties.ConnectionDetails;

import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
