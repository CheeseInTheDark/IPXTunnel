package ipxtunnel.server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class AddNodeHandler extends Thread implements Runnable
{
	private DatagramPacket addResponse;
	
	public AddNodeHandler(DatagramPacket addResponse)
	{
		this.addResponse = addResponse;
	}
	
	@Override
	public void run()
	{
		byte[] buffer = addResponse.getData();
		try
		{
			InetAddress clientAddress = InetAddress.getByAddress(Arrays.copyOfRange(buffer, 10, 14));
			InetAddress localAddress = InetAddress.getByAddress(Arrays.copyOfRange(buffer, 15, 19));
			
			int realPort = (0x0000FF00 & (buffer[20] << 8)) |
							(0x000000FF & buffer[21]);
			
			int fakePort = (0x0000FF00 & (buffer[23] << 8)) |
							(0x000000FF & buffer[24]);
			
			RealNodeAddress realNode = new RealNodeAddress(clientAddress, localAddress, realPort);
			FakeNodeAddress fakeNode = new FakeNodeAddress(addResponse.getAddress(), fakePort);
			
			if (!RoutingTable.nodeExists(realNode))
			{
				System.err.println("ERRROR: Attempted to add fake nodes for nonexistent real node");
			}
			else
			{
				System.out.println("Added new fakeNode: realNode=" +
					realNode.getClientAddress().getHostAddress().toString() + ", fakeNode=" +
					fakeNode.getClientAddress().getHostAddress().toString() + ":" +
					fakeNode.getPort());
			}
			RoutingTable.addNode(realNode, fakeNode);
		}
		catch (UnknownHostException e)
		{
			System.err.println("ERROR: Got invalid address from add node response");
		}
	}
}
