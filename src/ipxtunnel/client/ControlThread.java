package ipxtunnel.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class ControlThread extends Thread implements Runnable
{
	DatagramSocket serverSocket;
	DatagramSocket tunnelSocket;
	int serverPort;
	InetAddress serverAddress;
	
	HashMap<Integer, WrapperListener> wrapperListeners = new HashMap<Integer, WrapperListener>();
	
	public ControlThread(DatagramSocket serverSocket, 
			DatagramSocket tunnelSocket,
			int serverPort,
			InetAddress serverAddress)
	{
		this.serverSocket = serverSocket;
		this.tunnelSocket = tunnelSocket;
		this.serverPort = serverPort;
		this.serverAddress = serverAddress;
	}
	
	@Override
	public void run()
	{
		byte[] buffer = new byte[IPXTunnelClient.MAX_BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		while(true)
		{
			try
			{
				serverSocket.receive(packet);
			}
			catch (IOException e)
			{
				System.err.println("ERROR: Could not receive server message");
				e.printStackTrace();
			}
			String message = new String(packet.getData()).substring(0, packet.getLength());
			
			if (message.startsWith("addnode:"))
			{
				addNode(message.substring(8));
			}
			else if (message.startsWith("removenode:"))
			{
				removeNode(message.substring(11));
			}
		}
	}
	
	private void addNode(String addRequest)
	{
		try
		{
			DatagramSocket ipxSocket = new DatagramSocket();
			
			IPXTunnelClient.ipxSockets.put(ipxSocket.getLocalPort(), ipxSocket);
			WrapperListener listener = new WrapperListener(ipxSocket, tunnelSocket);
			wrapperListeners.put(ipxSocket.getLocalPort(), listener);
			listener.start();
			
			String packetData = "addednode:" + addRequest + ":  ";
			byte[] buffer = packetData.getBytes();
			buffer[buffer.length - 2] = (byte) (ipxSocket.getLocalPort() >> 8);
			buffer[buffer.length - 1] = (byte) (ipxSocket.getLocalPort());
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			packet.setAddress(serverAddress);
			packet.setPort(serverPort);
			try
			{
				serverSocket.send(packet);
				System.out.println("Sent notice to server for new fake node on port " + ipxSocket.getLocalPort());
			}
			catch (IOException e)
			{
				System.out.println("ERROR: Could not send node addition confirmation to server");
				e.printStackTrace();
			}
		}
		catch (SocketException e)
		{
			System.out.println("ERROR: Could not add new IPX node");
			e.printStackTrace();
		}
	}
	
	private void removeNode(String node)
	{
		int port = Integer.parseInt(node);
		wrapperListeners.get(port).kill();
		wrapperListeners.remove(Integer.parseInt(node));
	}
}
