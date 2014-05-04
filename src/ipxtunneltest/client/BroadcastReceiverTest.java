package ipxtunneltest.client;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import ipxtunnel.client.broadcasts.BroadcastReceiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class BroadcastReceiverTest {

	@Mock
	private MulticastSocket broadcastSocket;
	
	@Test
	public void testBroadcastReceiverReceivesPackets() throws IOException
	{
		MockitoAnnotations.initMocks(this);
		BroadcastReceiver receiver = new BroadcastReceiver(broadcastSocket);
		
		receiver.listen();
		
		verify(broadcastSocket).receive(any(DatagramPacket.class));
	}

	@Test
	public void testBroadcastReceiverReturnsReceivedPacket() throws IOException
	{
		MockitoAnnotations.initMocks(this);
		BroadcastReceiver receiver = new BroadcastReceiver(broadcastSocket);
		
		DatagramPacket packet = new DatagramPacket(new byte[]{0x00, 0x01}, 2);
		doAnswer(
				new Answer() 
				{
					@Override
					public Void answer(InvocationOnMock invocation)
					{
						DatagramPacket packet = (DatagramPacket) invocation.getArguments()[0];
						packet.setData(new byte[]{0x00, 0x01});
						return null;
					}
				}
		).when(broadcastSocket).receive(any(DatagramPacket.class));
		
		DatagramPacket receivedPacket = receiver.listen();
		assertArrayEquals(packet.getData(), receivedPacket.getData());
	}
}
