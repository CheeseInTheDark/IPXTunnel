package ipxtunnel.common;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class IPXPacketInjector {

	private DatagramPacket packet;
	
	public IPXPacketInjector(DatagramPacket packet)
	{
		this.packet = packet;
		
		byte[] messageWithRoutingInfo = Arrays.copyOf(packet.getData(), packet.getLength() + 9);
		packet.setData(messageWithRoutingInfo);
	}

	public void injectSender(InetAddress sender)
	{
		byte[] senderBytes = sender.getAddress();
		
		packet.setData(senderBytes);
	}

	public void injectSenderPort(int port)
	{
		byte[] portBytes = {(byte) (port >> 8), (byte) port};
		
		byte[] packetBytes = Arrays.copyOf(packet.getData(), packet.getLength());
		
		packetBytes[packet.getLength() - 5] = portBytes[0];
		packetBytes[packet.getLength() - 4] = portBytes[1];
		
		packet.setData(packetBytes);
	}
}
