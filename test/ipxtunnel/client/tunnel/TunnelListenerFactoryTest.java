package ipxtunnel.client.tunnel;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.socketwrappers.PacketListener;

import java.io.IOException;
import java.net.DatagramPacket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TunnelListenerFactoryTest
{
	@Mock
	private PacketListener listener;
	
	@Mock
	private TunnelHandler tunnelHandler;
	
	private DatagramPacket packet;

	@Before
	public void setup() throws IOException
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldConstructTunnelListenerWithProperHandler() throws IOException 
	{
		TunnelListenerFactory underTest = new TunnelListenerFactory();
		
		MiddleMan constructedMiddleMan = underTest.construct(listener, tunnelHandler);
		
		assertThat(constructedMiddleMan, is(notNullValue()));
	}
}
