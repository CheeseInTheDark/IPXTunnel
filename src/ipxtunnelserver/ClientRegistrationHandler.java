package ipxtunnelserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientRegistrationHandler extends Thread implements Runnable
{
	private DatagramPacket registrationRequest;
	private DatagramSocket serverSocket;
	
	public ClientRegistrationHandler(DatagramSocket serverSocket, DatagramPacket registrationRequest)
	{
		this.serverSocket = serverSocket;
		this.registrationRequest = registrationRequest;
	}
	
	@Override
	public void run()
	{
		addClient(registrationRequest);
	}
	
	private void addClient(DatagramPacket packet)
	{
		InetAddress clientAddress = packet.getAddress();
		int port = Integer.parseInt(new String(packet.getData()).substring(0, packet.getLength()).split(":")[1]);
		int infoPort = packet.getPort();
		
		Client newClient = new Client(clientAddress, port, infoPort);
		IPXTunnelServer.clients.add(newClient);
		sendConfirmation(newClient, infoPort);
		
		System.out.println("Added client: " + clientAddress.getHostAddress() + ":" + String.valueOf(port));
	}

	private void sendConfirmation(Client client, int infoPort)
	{
		RealNodeAddress[] realNodes = RoutingTable.getRealNodes();
		String requestString = "okay:";
		
		for (RealNodeAddress realNode : realNodes)
		{ 
			requestString += new String(realNode.getClientAddress().getAddress()) + ":" +
				new String(realNode.getLocalAddress().getAddress()) + ":" +
				String.valueOf(realNode.getPort());
		}
		
		byte[] buffer = requestString.getBytes();
		DatagramPacket response = new DatagramPacket(buffer, buffer.length);
		response.setAddress(client.address());
		response.setPort(client.infoPort());
		trySend(response);
	}
	
	private void trySend(DatagramPacket packet)
	{
		try
		{
			serverSocket.send(packet);
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not send registration confirmation to client");
			e.printStackTrace();
		}
	}
}
