package ipxtunnel.client.broadcast;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListener;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BroadcastListenerFactoryTest
{
    private BroadcastListenerFactory underTest = new BroadcastListenerFactory();
    
    @Mock
    private ConnectionDetails connectionDetails;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldBuildBroadcastListener() throws IOException
    {
        PacketListener listener = underTest.construct(connectionDetails);
        
        assertThat(listener, is(notNullValue()));
    }
}
