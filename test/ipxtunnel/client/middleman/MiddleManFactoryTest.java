package ipxtunnel.client.middleman;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManFactory;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.socketwrappers.PacketListener;

import java.io.IOException;
import java.net.DatagramPacket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MiddleManFactoryTest
{
	@Mock
	private PacketListener listener;
	
	@Mock
	private PacketHandler packetHandler;
	
	@Before
	public void setup() throws IOException
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldConstructTunnelListenerWithProperHandler() throws IOException 
	{
		MiddleManFactory underTest = new MiddleManFactory();
		
		MiddleMan constructedMiddleMan = underTest.construct(listener, packetHandler);
		
		assertThat(constructedMiddleMan, is(notNullValue()));
	}
}
