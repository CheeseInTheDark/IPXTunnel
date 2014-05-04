package ipxtunnel.client.broadcasts;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class BroadcastReceiver
{
	private MulticastSocket broadcastSocket;
	
	public BroadcastReceiver(MulticastSocket broadcastSocket)
	{
		this.broadcastSocket = broadcastSocket;
	}

	public DatagramPacket listen() throws IOException
	{
		DatagramPacket packet = new DatagramPacket(new byte[0], 0);
		broadcastSocket.receive(packet);
		return packet;
	}

}
