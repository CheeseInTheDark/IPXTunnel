package ipxtunnel.server;

import java.net.InetAddress;

public class Client
{
	private InetAddress address;
	private int port;
	private int infoPort;
	
	public Client(InetAddress address, int port, int infoPort)
	{
		this.address = address;
		this.port = port;
		this.infoPort = infoPort;
	}
	
	public InetAddress address()
	{
		return address;
	}
	
	public int port()
	{
		return port;
	}
	
	public int infoPort()
	{
		return infoPort;
	}
}
