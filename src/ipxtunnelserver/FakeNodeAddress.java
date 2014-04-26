package ipxtunnelserver;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class FakeNodeAddress
{
	private InetAddress clientAddress;
	private int port;
	
	public FakeNodeAddress(InetAddress clientAddress, int port)
	{
		this.clientAddress = clientAddress;
		this.port = port;
	}
	
	public FakeNodeAddress(DatagramPacket packet)
	{
		this.clientAddress = packet.getAddress();
		int ipxLength = packet.getLength();
		byte[] buffer = Arrays.copyOf(packet.getData(), packet.getLength());
		
		this.port = (int) ((buffer[ipxLength - 2] << 8) & 0x0000FF00) | (buffer[ipxLength - 1] & 0x000000FF);
	}

	public InetAddress getClientAddress()
	{
		return clientAddress;
	}
	
	public int getPort()
	{
		return port;
	}
	
	@Override
	public boolean equals(Object other)
	{
		boolean equal = true;
		
		if (other instanceof FakeNodeAddress)
		{
			FakeNodeAddress otherNode = (FakeNodeAddress) other;
			equal &= otherNode.getPort() == getPort();
			equal &= otherNode.getClientAddress().equals(getClientAddress());
		}
		else
		{
			equal = false;
		}
		
		return equal;
	}
}
