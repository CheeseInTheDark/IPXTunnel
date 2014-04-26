package ipxtunnelserver;
import java.io.*;
import java.util.*;
import java.net.*;


public class IPXTunnelServer
{
	static int MAX_BUFFER_SIZE = 4096;
	static ArrayList<Client> clients = new ArrayList<Client>();
	
	public static void main(String[] args)
	{
		if (args.length < 2)
		{
			System.err.println("Usage: java tunnel-port server-port");
			System.err.println();
			System.err.println("The tunnel-port is the port used to send and receive tunneled IPX packets.");
			System.err.println("The server-port is used for clients to communicate with the server.");
			System.exit(1);
		}

		int tunnelPort = Integer.parseInt(args[0]);
		int serverPort = Integer.parseInt(args[1]);
		
		try
		{
			DatagramSocket tunnelSocket = new DatagramSocket(tunnelPort);
			DatagramSocket serverSocket = new DatagramSocket(serverPort);
			
			ClientListener serverThread = new ClientListener(serverSocket);
			serverThread.start();
			
			IPXListener ipxListener = new IPXListener(tunnelSocket, serverSocket);
			ipxListener.start();
		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e.getMessage());
			return;
		}
	}
}
