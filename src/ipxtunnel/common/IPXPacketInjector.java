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
		injectInformation(senderBytes, 8);
	}

	public void injectSenderPort(int port)
	{
		injectInformation(portToBytes(port), 4);
	}
	
	public void injectDestinationPort(int port)
	{
		injectInformation(portToBytes(port), 2);
	}

	private byte[] portToBytes(int port)
	{
		return new byte[]{(byte) (port >> 8), (byte) port};
	}
	
	public void injectPacketType(byte type)
	{
		injectInformation(new byte[]{type}, 9);
	}
	
	private void injectInformation(byte[] information, int offset)
	{
		int packetLength = packet.getLength();
		int startIndex = packetLength - offset;
		
		packet.setData(createNewData(information, startIndex));
	}
	
	private byte[] createNewData(byte[] information, int startIndex)
	{
		byte[] packetBytes = packetBytes();
		for (int infoByte = 0; infoByte < information.length; infoByte++)
		{
			packetBytes[startIndex + infoByte] = information[infoByte];
		}
		
		return packetBytes;
	}
	
	private byte[] packetBytes()
	{
		return Arrays.copyOf(packet.getData(), packet.getLength());
	}
}
