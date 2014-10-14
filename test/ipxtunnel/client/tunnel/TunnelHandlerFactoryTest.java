package ipxtunnel.client.tunnel;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TunnelHandlerFactoryTest
{
    private TunnelHandlerFactory tunnelHandlerFactory;
    
    @Mock
    private NodeDelegates nodeDelegates;
    
    @Before
    public void setup()
    {
        tunnelHandlerFactory = new TunnelHandlerFactory();
    }
    
    @Test
    public void shouldConstructTunnelHandler()
    {
        TunnelHandler constructedHandler = tunnelHandlerFactory.construct(nodeDelegates);
        
        assertThat(constructedHandler, is(notNullValue()));
    }
}
