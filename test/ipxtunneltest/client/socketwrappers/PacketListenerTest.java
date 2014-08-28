package ipxtunneltest.client.socketwrappers;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import ipxtunnel.client.socketwrappers.PacketListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class PacketListenerTest {

	@Mock
	private DatagramSocket socket;
	
	@Test
	public void testBroadcastReceiverReceivesPackets() throws IOException
	{
		MockitoAnnotations.initMocks(this);
		PacketListener listener = new PacketListener(socket);
		
		listener.listen();
		
		verify(socket).receive(any(DatagramPacket.class));
	}

	@Test
	public void testBroadcastListenerReturnsReceivedPacket() throws IOException
	{
		MockitoAnnotations.initMocks(this);
		PacketListener listener = new PacketListener(socket);
		
		DatagramPacket packet = new DatagramPacket(new byte[]{0x00, 0x01}, 2);
		doAnswer(new PacketAnswer()).when(socket).receive(any(DatagramPacket.class));
		
		DatagramPacket receivedPacket = listener.listen();
		assertArrayEquals(packet.getData(), receivedPacket.getData());
	}
	
	private class PacketAnswer implements Answer<Void>
	{
        @Override
        public Void answer(InvocationOnMock invocation)
        {
            DatagramPacket packet = (DatagramPacket) invocation.getArguments()[0];
            packet.setData(new byte[]{0x00, 0x01});
            return null;
        }	    
	}
}
