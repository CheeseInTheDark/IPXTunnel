package ipxtunnelclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.Arrays;

public class BroadcastListener extends Thread implements Runnable 
{
	private MulticastSocket broadcastSocket;
	private DatagramSocket tunnelSocket;
	
	public BroadcastListener(MulticastSocket broadcastSocket, 
			DatagramSocket tunnelSocket)
	{
		this.broadcastSocket = broadcastSocket;
		this.tunnelSocket = tunnelSocket;
	}
	
	@Override
	public void run()
	{
		byte[] buffer = new byte[IPXTunnelClient.MAX_BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		while (true)
		{
			tryReceive(packet);
			trySend(injectSenderData(packet));
		}
	}
	
	private void tryReceive(DatagramPacket packet)
	{
		try
		{
			do
			{
				broadcastSocket.receive(packet);
				System.out.println("Got broadcast packet");
			}
			while (IPXTunnelClient.ipxSockets.containsKey(packet.getPort()));
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not receive broadcast packet from IPXWrapper");
			e.printStackTrace();
		}
	}
	
	private void trySend(DatagramPacket packet)
	{
		packet.setPort(IPXTunnelClient.tunnelPort);
		packet.setAddress(IPXTunnelClient.serverAddress);
		try
		{
			tunnelSocket.send(packet);
			System.out.println("Sent broadcast packet to server");
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could not send broadcast packet to server");
			e.printStackTrace();
		}		
	}
	
	private DatagramPacket injectSenderData(DatagramPacket packet)
	{
		// packet structure:
		// IPX packet contents
		// 1 byte - packet type (broadcast = 0, point to point = 1)
		// 4 bytes - IPXWrapper sender address
		// 2 bytes - IPXWrapper sender port
		// 2 bytes - Not used for broadcasts
		
		byte[] ipxBuffer = Arrays.copyOf(packet.getData(), packet.getLength());
		int bufferLength = ipxBuffer.length + 9;
		
		byte[] wrappedBuffer = Arrays.copyOf(ipxBuffer, bufferLength);
		wrappedBuffer[bufferLength - 9] = 0x00;
		wrappedBuffer[bufferLength - 8] = (byte) (packet.getAddress().getAddress()[0]);
		wrappedBuffer[bufferLength - 7] = (byte) (packet.getAddress().getAddress()[1]);
		wrappedBuffer[bufferLength - 6] = (byte) (packet.getAddress().getAddress()[2]);
		wrappedBuffer[bufferLength - 5] = (byte) (packet.getAddress().getAddress()[3]);
		wrappedBuffer[bufferLength - 4] = (byte) (packet.getPort() >> 8);
		wrappedBuffer[bufferLength - 3] = (byte) packet.getPort();
		wrappedBuffer[bufferLength - 2] = (byte) 0x00;
		wrappedBuffer[bufferLength - 1] = (byte) 0x00;
		
		return new DatagramPacket(wrappedBuffer, bufferLength);
	}
}
