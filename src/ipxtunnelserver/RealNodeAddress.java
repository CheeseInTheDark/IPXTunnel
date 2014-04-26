package ipxtunnelserver;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class RealNodeAddress
{
	private InetAddress routerAddress;
	private InetAddress localAddress;
	private int port;
	
	public RealNodeAddress(InetAddress routerAddress, InetAddress localAddress, int port)
	{
		this.routerAddress = routerAddress;
		this.localAddress = localAddress;
		this.port = port;
	}
	
	public RealNodeAddress(DatagramPacket packet)
	{
		this.routerAddress = packet.getAddress();
		int ipxLength = packet.getLength();
		byte[] buffer = Arrays.copyOf(packet.getData(), packet.getLength());
		try
		{
			this.localAddress = InetAddress.getByAddress(Arrays.copyOfRange(buffer, ipxLength - 8, ipxLength - 4));
		}
		catch (UnknownHostException e)
		{
			System.err.println("ERROR: Got invalid real node address");
			e.printStackTrace();
		}
		
		this.port = ((buffer[ipxLength - 4] << 8) & 0x0000FF00) |
					 (buffer[ipxLength - 3] & 0x000000FF);
	}
	
	public InetAddress getClientAddress()
	{
		return routerAddress;
	}
	
	public InetAddress getLocalAddress()
	{
		return localAddress;
	}
	
	public int getPort()
	{
		return port;
	}
	
	@Override
	public boolean equals(Object other)
	{
		boolean equal = true;
		equal &= other instanceof RealNodeAddress;
		if (equal)
		{
			RealNodeAddress otherNode = (RealNodeAddress) other;
			equal &= otherNode.getClientAddress().equals(getClientAddress());
			equal &= otherNode.getLocalAddress().equals(getLocalAddress());
			equal &= otherNode.getPort() == getPort();
		}
		return equal;
	}
	
	@Override
	public int hashCode()
	{
		return 1;
	}
}
