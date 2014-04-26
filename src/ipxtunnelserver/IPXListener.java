package ipxtunnelserver;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IPXListener extends Thread implements Runnable
{
	DatagramSocket tunnelSocket;
	private DatagramSocket serverSocket;

	public IPXListener(DatagramSocket tunnelSocket, DatagramSocket serverSocket)
	{
		this.tunnelSocket = tunnelSocket;
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run()
	{
		byte[] buffer = new byte[IPXTunnelServer.MAX_BUFFER_SIZE];
		DatagramPacket IPXPacket = new DatagramPacket(buffer, IPXTunnelServer.MAX_BUFFER_SIZE);
		
		while(true)
		{
			try
			{
				tunnelSocket.receive(IPXPacket);

				int ipxLength = IPXPacket.getLength();
				buffer = Arrays.copyOf(IPXPacket.getData(), IPXPacket.getLength());
				
				checkForNewNode(IPXPacket);
				
				if (buffer[ipxLength - 9] == 0x00)
				{
					handleBroadcast(IPXPacket);
				}
				else if (buffer[ipxLength - 9] == 0x01)
				{
					handleDirect(IPXPacket);
				}
			}
			catch (IOException e)
			{
				System.err.println("Could not receive client-bound IPX packet");
				e.printStackTrace();
			}
		}
	}
	
	private void handleBroadcast(DatagramPacket packet)
	{
		RealNodeAddress realNode = new RealNodeAddress(packet);
		ArrayList<FakeNodeAddress> destinations = RoutingTable.getFakeNodes(realNode);

		HashMap<InetAddress, Client> clients = new HashMap<InetAddress, Client>();
		
		for (Client client : IPXTunnelServer.clients)
		{
			clients.put(client.address(), client);
		}
		
		if (destinations != null)
		{
			for (FakeNodeAddress destination : destinations)
			{
				byte[] buffer = Arrays.copyOf(packet.getData(), packet.getLength());
				buffer[packet.getLength() - 2] = (byte) (destination.getPort() >> 8);
				buffer[packet.getLength() - 1] = (byte) destination.getPort();
				
				for (RealNodeAddress sender : RoutingTable.getRealNodesForClient(destination.getClientAddress()))
				{
					buffer[packet.getLength() - 8] = sender.getLocalAddress().getAddress()[0];
					buffer[packet.getLength() - 7] = sender.getLocalAddress().getAddress()[1];
					buffer[packet.getLength() - 6] = sender.getLocalAddress().getAddress()[2];
					buffer[packet.getLength() - 5] = sender.getLocalAddress().getAddress()[3]; 
					buffer[packet.getLength() - 4] = (byte) (sender.getPort() >> 8);
					buffer[packet.getLength() - 3] = (byte) sender.getPort();
				
					DatagramPacket broadcastPacket = new DatagramPacket(buffer, buffer.length);
					
					broadcastPacket.setAddress(destination.getClientAddress());
					broadcastPacket.setPort(clients.get(destination.getClientAddress()).port());
					try
					{
						tunnelSocket.send(broadcastPacket);
					}
					catch (IOException e)
					{
						System.err.println("ERROR: Could not send broadcast packet");
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void handleDirect(DatagramPacket packet)
	{
		RealNodeAddress sendingNode = new RealNodeAddress(packet);
		FakeNodeAddress receivingNode = new FakeNodeAddress(packet);
		
		RealNodeAddress destinationReceiver = RoutingTable.getRealNodeForFakeNode(receivingNode);
		FakeNodeAddress destinationSender = RoutingTable.getFakeNodeForClientAndRealNode(sendingNode, destinationReceiver.getClientAddress());
		
		DatagramPacket injectedPacket = injectDirectPacketInformation(packet, destinationReceiver, destinationSender);
		try
		{
			tunnelSocket.send(injectedPacket);
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not send point-to-point IPX packet");
			e.printStackTrace();
		}
	}
	
	private DatagramPacket injectDirectPacketInformation(DatagramPacket packet, RealNodeAddress receiver, FakeNodeAddress sender)
	{
		byte[] buffer = Arrays.copyOf(packet.getData(), packet.getLength());
		int ipxLength = packet.getLength();
		buffer[ipxLength - 9] = 0x01;
		buffer[ipxLength - 8] = receiver.getLocalAddress().getAddress()[0];
		buffer[ipxLength - 7] = receiver.getLocalAddress().getAddress()[1];
		buffer[ipxLength - 6] = receiver.getLocalAddress().getAddress()[2];
		buffer[ipxLength - 5] = receiver.getLocalAddress().getAddress()[3];
		buffer[ipxLength - 4] = (byte) ((receiver.getPort() >> 8) & 0xFF);
		buffer[ipxLength - 3] = (byte) (receiver.getPort() & 0xFF);
		buffer[ipxLength - 2] = (byte) ((sender.getPort() >> 8) & 0xFF);
		buffer[ipxLength - 1] = (byte) (sender.getPort() & 0xFF);

		DatagramPacket injectedPacket = new DatagramPacket(buffer, buffer.length);
		injectedPacket.setAddress(sender.getClientAddress());
		
		for (Client client : IPXTunnelServer.clients)
		{
			if (client.address().equals(sender.getClientAddress()))
			{
				injectedPacket.setPort(client.port());
				break;
			}
		}		
		
		return injectedPacket;
	}
	
	private void checkForNewNode(DatagramPacket packet)
	{
		RealNodeAddress realNode = new RealNodeAddress(packet);
		if (!RoutingTable.nodeExists(realNode))
		{
			handleNewNode(realNode);
		}
	}
	
	private void handleNewNode(RealNodeAddress realNode)
	{
		RoutingTable.addNode(realNode);
		
		int initialClients = IPXTunnelServer.clients.size() - 1;
		for (Client client : IPXTunnelServer.clients)
		{
			if (!client.address().equals(realNode.getClientAddress()))
			{
				requestAddNode(client, realNode);
			}
		}

		try
		{
			while (RoutingTable.fakeNodeCountFor(realNode) < initialClients)
			{
				Thread.sleep(500);
			}
			
			System.out.println("Added new node: " + realNode.getClientAddress().getHostAddress().toString());
		}
		catch (InterruptedException e)
		{
			System.err.println("ERROR: IPXListener thread interrupted during wait");
			e.printStackTrace();
		}
	}
	
	private void requestAddNode(Client client, RealNodeAddress realNode)
	{
		byte[] realPort = new byte[2];
		realPort[0] = (byte) (realNode.getPort() >> 8);
		realPort[1] = (byte) (realNode.getPort());
		
		String requestString = "addnode:" + 
				new String(realNode.getClientAddress().getAddress()) + ":" +
				new String(realNode.getLocalAddress().getAddress()) + ":" +
				new String(realPort);
				
		byte[] buffer = requestString.getBytes();
		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
		request.setAddress(client.address());
		request.setPort(client.infoPort());
		try
		{
			serverSocket.send(request);
			System.out.println("Sent request to "  + client.address().getHostAddress().toString() +
					" to add fake node for real node " + realNode.getClientAddress().getHostAddress().toString() + ":" +
					(((realPort[0] << 8) & 0x0000FF00) | (0x000000FF & realPort[1])));
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not send request to client to add node");
			e.printStackTrace();
		}
	}
}
