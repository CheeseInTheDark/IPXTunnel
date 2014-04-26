package ipxtunnel.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ShutdownThread extends Thread implements Runnable 
{
	private InetAddress serverAddress;
	private DatagramSocket serverSocket;
	private int serverPort;
	
	public ShutdownThread(DatagramSocket serverSocket, 
			int serverPort, 
			InetAddress serverAddress)
	{
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run()
	{
		byte[] buffer = "removeme".getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		packet.setAddress(serverAddress);
		packet.setPort(serverPort);
		
		try
		{
			serverSocket.send(packet);
			System.out.println("Unregistered client");
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not unregister from server");
			e.printStackTrace();
		}
	}
	
	
}
