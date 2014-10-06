package ipxtunnel.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TunnelListenerThread extends Thread implements Runnable
{
	private DatagramSocket tunnelSocket;
	
	public TunnelListenerThread(DatagramSocket tunnelSocket)
	{
		this.tunnelSocket = tunnelSocket;
	}
	
	@Override
	public void run()
	{
		byte[] buffer = new byte[IPXTunnelClient.MAX_BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, IPXTunnelClient.MAX_BUFFER_SIZE);
		
		while(true)
		{
			tryReceive(packet);
			trySend(packet);
		}
	}
	
	private void tryReceive(DatagramPacket packet)
	{
		try
		{
			tunnelSocket.receive(packet);
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not receive IPX packet from server");
			e.printStackTrace();
		}
	}
	
	private void trySend(DatagramPacket packet)
	{
		int length = packet.getLength();
		
		byte[] strippedBuffer = Arrays.copyOf(packet.getData(), length - 9);
		byte[] fullBuffer = Arrays.copyOf(packet.getData(), length);

		DatagramPacket strippedPacket = new DatagramPacket(strippedBuffer, strippedBuffer.length);
		
		if (fullBuffer[length - 9] == 0x00)
		{
			int senderPort = ((fullBuffer[length - 2] << 8) & 0x0000FF00) |
						(fullBuffer[length - 1] & 0x000000FF);
			
			int destinationPort = ((fullBuffer[length - 4] << 8) & 0x0000FF00) |
					(fullBuffer[length - 3] & 0x000000FF);
			
			byte[] destinationAddress = new byte[4];
			destinationAddress[0] = fullBuffer[length - 8];
			destinationAddress[1] = fullBuffer[length - 7];
			destinationAddress[2] = fullBuffer[length - 6];
			destinationAddress[3] = fullBuffer[length - 5];
			
			try
			{
				strippedPacket.setAddress(InetAddress.getByAddress(destinationAddress));
				strippedPacket.setPort(destinationPort);
			}
			catch
			(UnknownHostException e1)
			{
				System.out.println("ERROR: Got invalid IPX node destination address");
				e1.printStackTrace();
			}
			
			try
			{
				IPXTunnelClient.ipxSockets.get(senderPort).send(strippedPacket);
				System.out.println("Sent broadcast packet to network from node on port " + senderPort + " to real node on port " + destinationPort);
			}
			catch (IOException e)
			{
				System.out.println("ERROR: Could not send packet to local IPX node");
				e.printStackTrace();
			}			
		}
		else if (fullBuffer[length - 9] == 0x01)
		{
			byte[] destinationAddress = Arrays.copyOfRange(fullBuffer, length - 8, length - 4); 
			
			int destinationPort = ((fullBuffer[length - 4] << 8) & 0x0000FF00) | (fullBuffer[length - 3] & 0x000000FF);
			int senderPort = ((fullBuffer[length - 2] << 8) & 0x0000FF00) | (fullBuffer[length - 1] & 0x000000FF);
			
			try
			{
				strippedPacket.setAddress(InetAddress.getByAddress(destinationAddress));
			}
			catch
			(UnknownHostException e1)
			{
				System.out.println("ERROR: Got invalid IPX node destination address");
				e1.printStackTrace();
			}
			
			strippedPacket.setPort(destinationPort);
			
			try
			{
				IPXTunnelClient.ipxSockets.get(senderPort).send(strippedPacket);
				System.out.println("Sent point-to-point packet from fake node on port " + senderPort +
						" to real node on port " + destinationPort);
			}
			catch (IOException e)
			{
				System.out.println("ERROR: Could not send packet to local IPX node");
				e.printStackTrace();
			}
		}
	}
}
