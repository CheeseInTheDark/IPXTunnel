package ipxtunnel.client;

import ipxtunnel.client.broadcasts.BroadcastListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

public class IPXTunnelClient
{
	static int networkInterface = 0;
	static HashMap<Integer, DatagramSocket> ipxSockets = new HashMap<Integer, DatagramSocket>();
	static int MAX_BUFFER_SIZE = 4096;
	static InetAddress broadcastAddress;
	static InetAddress serverAddress = null;
	static int serverPort;
	static int tunnelPort;
	
	static private HashMap<Integer, WrapperListener> initialWrappers = new HashMap<Integer, WrapperListener>();
	
	public static void main(String args[])
	{
		if (args.length < 4)
		{
			System.err.println("Usage: java IPX-port tunnel-port server-port server-ip");
			System.err.println();
			System.err.println("The IPX-port is the port to listen for broadcasts from IPXWrappers.");
			System.err.println("The tunnel-port is the port the server uses to send and receive tunneled IPX packets.");
			System.err.println("The server-port is the port the server uses.");
			System.err.println("The server-ip is the ip address of the server.");
			System.exit(1);
		}
		
		try
		{
			broadcastAddress = InetAddress.getByName("224.0.0.3");
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not get broadcast address.");
			e.printStackTrace();
			System.exit(1);
		}
		
		int broadcastPort = Integer.parseInt(args[0]);
		tunnelPort = Integer.parseInt(args[1]);
		serverPort = Integer.parseInt(args[2]);
		
		try
		{
			serverAddress = InetAddress.getByName(args[3]);
		}
		catch (UnknownHostException e1)
		{
			System.err.println("ERROR: Server ip address is invalid");
			e1.printStackTrace();
			System.exit(1);
		}
		
		networkInterface = Integer.parseInt(args[4]);
		
		try
		{
			MulticastSocket broadcastSocket = new MulticastSocket(broadcastPort);
			broadcastSocket.joinGroup(broadcastAddress);
			broadcastSocket.setTimeToLive(0);
			
			DatagramSocket tunnelSocket = new DatagramSocket();
			DatagramSocket serverSocket = new DatagramSocket();
			
			registerWithServer(serverSocket, tunnelSocket, serverPort, serverAddress, tunnelSocket.getLocalPort());
			
			TunnelListener tunnelListener = new TunnelListener(broadcastSocket, tunnelSocket);
			tunnelListener.start();
			
			BroadcastListener wrapperListener = new BroadcastListener(broadcastSocket, tunnelSocket);
			wrapperListener.start();
			
			ControlThread controlThread = new ControlThread(serverSocket, tunnelSocket, serverPort, serverAddress);
			controlThread.start();
		}			
		catch (IOException e)
		{
			System.err.println("IOException: " + e.getMessage());
			return;
		}
	}
	
	private static void registerWithServer(DatagramSocket serverSocket,
			DatagramSocket tunnelSocket, 
			int serverPort, 
			InetAddress serverAddress,
			int localTunnelPort)
	{
		byte[] buffer = ("addme:" + String.valueOf(localTunnelPort)).getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		packet.setAddress(serverAddress);
		packet.setPort(serverPort);

		try
		{
			serverSocket.send(packet);
			System.out.println("Sent server registration");
			
			Runtime.getRuntime().addShutdownHook(new ShutdownThread(serverSocket, serverPort, serverAddress));
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not send registration with server.");
			e.printStackTrace();
			System.exit(1);
		}
		
		try
		{
			serverSocket.receive(packet);
			if (new String(packet.getData()).substring(0, packet.getLength()).startsWith("okay"))
			{
				addInitialNodes(serverSocket, tunnelSocket, packet);
				System.out.println("Received registration confirmation from server, adding fake nodes, if any");
			}
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not receive server registration confirmation");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static void addInitialNodes(DatagramSocket serverSocket, 
			DatagramSocket tunnelSocket, 
			DatagramPacket packet)
	{
		byte[] nodes = Arrays.copyOfRange(packet.getData(), 5, packet.getLength());

		byte[] response = new byte[25];
		for (int node = 0; node < nodes.length / 12; node++)
		{
			DatagramSocket ipxSocket;
			try 
			{
				ipxSocket = new DatagramSocket();
				int localPort = ipxSocket.getLocalPort();
				System.out.println("Added node on " + localPort);
				ipxSockets.put(localPort, ipxSocket);
				WrapperListener listener = new WrapperListener(ipxSocket, tunnelSocket);
				listener.run();
				initialWrappers.put(localPort, listener);
				
				String packetData = "addednode:" + new String(Arrays.copyOfRange(packet.getData(), 9 + (node * 12), 9 + ((node + 1) * 12))) + ":" + String.valueOf(ipxSocket.getLocalPort());
				response = packetData.getBytes();
				DatagramPacket addpacket = new DatagramPacket(response, response.length);
				packet.setAddress(serverAddress);
				packet.setPort(serverPort);
				try
				{
					serverSocket.send(addpacket);
				}
				catch (IOException e)
				{
					System.out.println("ERROR: Could not send node addition confirmation to server");
					e.printStackTrace();
				}
			}
			catch (SocketException e)
			{
				System.err.println("ERROR: Could not add new IPX node");
				e.printStackTrace();
			}
		}	
	}
}
