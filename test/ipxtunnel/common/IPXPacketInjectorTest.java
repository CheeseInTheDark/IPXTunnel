package ipxtunnel.common;

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
		packet = constructPacket();
		injector = new IPXPacketInjector(packet);
	}
	
	private DatagramPacket constructPacket()
	{
		return new DatagramPacket(new byte[]{}, 0);
	}
	
	@Test
	public void testIPXPacketInjectorShouldAddRoutingInformationOnConstruction()
	{
		DatagramPacket packet = constructPacket();
		assertEquals(0, packet.getLength());
		
		new IPXPacketInjector(packet);
		
		assertEquals(9, packet.getLength());
	}
	
	@Test
	public void testIPXPacketInjectorCanInjectSenderIPAddress() throws UnknownHostException
	{
		InetAddress sender = InetAddress.getByName("127.0.0.1");
		injector.injectSender(sender);
		
		byte[] expectedMessage = InetAddress.getByName("127.0.0.1").getAddress();
		assertArrayEquals(expectedMessage, packetSegment(8, 4));
	}
	
	@Test
	public void testIPXPacketInjectorCanInjectSenderPort()
	{
		int port = 12321;
		injector.injectSenderPort(port);
		
		byte[] expectedMessage = {0x30, 0x21};
		assertArrayEquals(expectedMessage, packetSegment(4, 2));
	}

	@Test
	public void testIPXPacketInjectorCanInjectDestinationPort()
	{
		int port = 12320;
		injector.injectDestinationPort(port);
		
		byte[] expectedMessage = {0x30, 0x20};
		assertArrayEquals(expectedMessage, packetSegment(2, 0));
	}
	
	@Test
	public void testIPXPacketInjectorCanInjectPacketType()
	{
		byte broadcastType = 0x00;
		injector.injectPacketType(broadcastType);
		
		byte expectedType = 0x00;
		assertEquals(expectedType, packetSegment(9, 8)[0]);
	}
	
	private byte[] packetSegment(int startOffset, int endOffset)
	{
		return Arrays.copyOfRange(
				packet.getData(), packet.getLength() - startOffset,
				packet.getLength() - endOffset);
	}
}
