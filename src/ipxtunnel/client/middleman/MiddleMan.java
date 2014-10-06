package ipxtunnel.client.middleman;

import ipxtunnel.client.socketwrappers.PacketListener;

import java.io.IOException;
import java.net.DatagramPacket;

public class MiddleMan
{
	private PacketListener listener;
	private PacketHandler handler;
	
	public MiddleMan(PacketListener listener, PacketHandler handler)
	{
		this.listener = listener;
		this.handler = handler;
	}
	
	public void handleOnePacket() throws IOException 
	{
		DatagramPacket packet = listener.listen();
		handler.handle(packet);
	}
}
