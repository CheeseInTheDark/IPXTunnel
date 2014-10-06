package ipxtunnel.client.socketwrappers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class PacketListener
{
	private DatagramSocket socket;
	
	public PacketListener(DatagramSocket socket)
	{
		this.socket = socket;
	}

	public DatagramPacket listen() throws IOException
	{
		DatagramPacket packet = new DatagramPacket(new byte[0], 0);
		socket.receive(packet);
		return packet;
	}

}
