package ipxtunnel.server;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class ClientRemovalHandler extends Thread implements Runnable
{
	private DatagramPacket removalRequest;
	
	public ClientRemovalHandler (DatagramPacket removalRequest)
	{
		this.removalRequest = removalRequest;
	}
	
	@Override
	public void run()
	{
		removeClient(removalRequest);
	}
	
	private void removeClient(DatagramPacket packet)
	{
		InetAddress clientAddress = packet.getAddress();
		int port = packet.getPort();
		
		for (int client = 0; client < IPXTunnelServer.clients.size(); client++)
		{
			Client removalCandidate = IPXTunnelServer.clients.get(client);
			if (removalCandidate.address().equals(clientAddress))
			{
				RoutingTable.removeClient(IPXTunnelServer.clients.get(client));
				IPXTunnelServer.clients.remove(client);
				break;
			}
		}
		
		System.out.println("Removed client: " + clientAddress.getHostAddress() + ":" + String.valueOf(port));
	}
}
