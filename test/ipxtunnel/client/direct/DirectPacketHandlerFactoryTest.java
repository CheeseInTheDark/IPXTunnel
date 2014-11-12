package ipxtunnel.client.direct;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.properties.ConnectionDetails;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class DirectPacketHandlerFactoryTest
{

    private DirectPacketHandlerFactory underTest = new DirectPacketHandlerFactory();
    
    @Mock
    private ConnectionDetails serverConnectionDetails;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldConstructPacketHandler()
    {
        DirectPacketHandler handler = underTest.construct(serverConnectionDetails);
        
        assertThat(handler, is(notNullValue()));
    }
}
