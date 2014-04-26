package ipxtunnelclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class WrapperListener extends Thread implements Runnable
{
	private DatagramSocket ipxSocket;
	private DatagramSocket tunnelSocket;
	
	private boolean run = true;
	
	public WrapperListener(DatagramSocket ipxSocket, 
			DatagramSocket tunnelSocket)
	{
		this.ipxSocket = ipxSocket;
		this.tunnelSocket = tunnelSocket;
	}
	
	@Override
	public void run()
	{
		byte[] buffer = new byte[IPXTunnelClient.MAX_BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		while (run)
		{
			tryReceive(packet);
			trySend(injectSourceAndDestinationData(packet));
		}
	}
	
	public void kill()
	{
		run = false;
	}
	
	private void tryReceive(DatagramPacket packet)
	{
		try
		{
			ipxSocket.receive(packet);
		}
		catch (IOException e)
		{
			System.out.println("ERROR: Could not receive broadcast packet from IPXWrapper");
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
			System.out.println("Sent point-to-point packet to server");
		}
		catch (IOException e)
		{
			System.out.println("ERROR: Could not send point to point packet to server");
			e.printStackTrace();
		}		
	}
	
	private DatagramPacket injectSourceAndDestinationData(DatagramPacket packet)
	{
		// packet structure:
		// IPX packet contents
		// 1 byte - packet type (broadcast = 0, point to point = 1)
		// 4 bytes - IPXWrapper sender IP
		// 2 bytes - IPXWrapper sender port
		// 2 bytes - IPXWrapper destination port
		
		byte[] ipxBuffer = Arrays.copyOf(packet.getData(), packet.getLength());
		int bufferLength = ipxBuffer.length + 9;
		
		byte[] wrappedBuffer = Arrays.copyOf(ipxBuffer, bufferLength);
		wrappedBuffer[bufferLength - 9] = 0x01;
		wrappedBuffer[bufferLength - 8] = (byte) (packet.getAddress().getAddress()[0]);
		wrappedBuffer[bufferLength - 7] = (byte) (packet.getAddress().getAddress()[1]);
		wrappedBuffer[bufferLength - 6] = (byte) (packet.getAddress().getAddress()[2]);
		wrappedBuffer[bufferLength - 5] = (byte) (packet.getAddress().getAddress()[3]);
		wrappedBuffer[bufferLength - 4] = (byte) (packet.getPort() >> 8);
		wrappedBuffer[bufferLength - 3] = (byte) packet.getPort();
		wrappedBuffer[bufferLength - 2] = (byte) (ipxSocket.getLocalPort() >> 8);
		wrappedBuffer[bufferLength - 1] = (byte) ipxSocket.getLocalPort();
		
		return new DatagramPacket(wrappedBuffer, bufferLength);
	}
}
