package ipxtunnel.client.socketwrappers;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import ipxtunnel.answers.PacketAnswer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PacketListenerTest {

	@Mock
	private DatagramSocket socket;
	
	@InjectMocks
	private PacketListener listener;
	
	@Before
	public void setup() 
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testListenerReceivesPackets() throws IOException
	{
		listener.listen();
		
		verify(socket).receive(any(DatagramPacket.class));
	}

	@Test
	public void testPacketListenerReturnsReceivedPacket() throws IOException
	{
		DatagramPacket packet = new DatagramPacket(new byte[]{0x00, 0x01}, 2, InetAddress.getLocalHost(), 123);
		doAnswer(new PacketAnswer(packet)).when(socket).receive(any(DatagramPacket.class));
		
		DatagramPacket receivedPacket = listener.listen();
		
		assertArrayEquals(packet.getData(), receivedPacket.getData());
	}
}
