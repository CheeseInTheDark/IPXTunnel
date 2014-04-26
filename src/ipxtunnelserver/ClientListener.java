package ipxtunnelserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientListener extends Thread implements Runnable
{
	private DatagramSocket serverSocket;
	
	public ClientListener(DatagramSocket serverSocket)
	{
		this.serverSocket = serverSocket;
	}

	@Override
	public void run()
	{
		DatagramPacket packet;
		byte[] buffer = new byte[IPXTunnelServer.MAX_BUFFER_SIZE];
		
		System.out.println("Receiving clients");
		
		while (true)
		{
			packet = new DatagramPacket(buffer, IPXTunnelServer.MAX_BUFFER_SIZE);
			tryReceive(packet);
			handlePacket(packet);
		}
	}

	private void tryReceive(DatagramPacket packet)
	{
		try
		{
			serverSocket.receive(packet);
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not receive information packet from client");
			e.printStackTrace();
		}
	}
	
	private void handlePacket(DatagramPacket packet)
	{
		String output = new String(packet.getData()).substring(0, packet.getLength());
		
		if (output.startsWith("addme:"))
		{
			(new ClientRegistrationHandler(serverSocket, packet)).start();
		}
		else if (output.compareTo("removeme") == 0)
		{
			(new ClientRemovalHandler(packet)).start();
		}
		else if (output.startsWith("addednode:"))
		{
			(new AddNodeHandler(packet)).start();
		}
	}
}
