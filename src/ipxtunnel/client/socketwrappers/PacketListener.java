package ipxtunnel.client.socketwrappers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;

public class PacketListener
{
	private DatagramSocket broadcastSocket;
	
	public PacketListener(DatagramSocket socket)
	{
		this.broadcastSocket = socket;
	}

	public DatagramPacket listen() throws IOException
	{
		DatagramPacket packet = new DatagramPacket(new byte[0], 0);
		broadcastSocket.receive(packet);
		return packet;
	}

}
