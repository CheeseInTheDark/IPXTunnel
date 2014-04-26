package ipxtunneltest.common;

import static org.junit.Assert.*;
import ipxtunnel.common.IPXPacketInjector;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class IPXPacketInjectorTest {

	private DatagramPacket packet;
	private IPXPacketInjector injector;
	
	@Before
	public void setUp()
	{
		byte[] message = new byte[0];
		packet = new DatagramPacket(message, 0);
		
		injector = new IPXPacketInjector(packet);
	}
	
	@Test
	public void testIPXPacketInjectorShouldAddRoutingInformationOnConstruction()
	{
		assertEquals(9, packet.getLength());
	}
	
	@Test
	public void testIPXPacketInjectorCanInjectSenderIPAddress() throws UnknownHostException
	{
		InetAddress sender = InetAddress.getByName("127.0.0.1");
		injector.injectSender(sender);
		
		byte[] expectedMessage = InetAddress.getByName("127.0.0.1").getAddress();
		assertArrayEquals(expectedMessage, Arrays.copyOf(packet.getData(), packet.getLength()));
	}
	
	@Test
	public void testIPXPacketInjectorCanInjectSenderPort()
	{
		int port = 12321;
		injector.injectSenderPort(port);
		
		byte[] expectedMessage = {0x30, 0x21};
		assertArrayEquals(expectedMessage, Arrays.copyOfRange(packet.getData(), packet.getLength() - 5, packet.getLength() - 3));
	}

}
