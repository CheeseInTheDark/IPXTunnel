package ipxtunnel.client.middleman;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ipxtunnel.client.socketwrappers.PacketListener;

import java.io.IOException;
import java.net.DatagramPacket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MiddleManTest {
	
	@InjectMocks
	private MiddleMan middleMan;
	
	@Mock
	private PacketListener listener;

	@Mock
	private PacketHandler handler;
	
	private DatagramPacket packet;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		packet = new DatagramPacket(new byte[0], 0);
	}
	
	@Test
	public void shouldListenForPacket() throws IOException
	{
		middleMan.handleOnePacket();
		
		verify(listener).listen();
	}
	
	@Test
	public void shouldHandlePacket() throws IOException
	{
		when(listener.listen()).thenReturn(packet);
		
		middleMan.handleOnePacket();
		
		verify(handler).handle(packet);
	}
}
