package ipxtunnel.common;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class IPXPacketUnpacker
{

    public IPXPacketUnpacker()
    {
    }
    
    public InetAddress extractSenderAddress(DatagramPacket packet) throws UnknownHostException
    {
        int length = packet.getLength();
        byte[] address = Arrays.copyOfRange(packet.getData(), length - 8, length - 4);
        return InetAddress.getByAddress(address);
    }

    public int extractSenderPort(DatagramPacket packet)
    {
        int length = packet.getLength();
        byte[] portBytes = Arrays.copyOfRange(packet.getData(), length - 4, length - 2);
        
        return portFromBytes(portBytes);
    }

	public int extractDestinationPort(DatagramPacket packet)
	{
        int length = packet.getLength();
        byte[] portBytes = Arrays.copyOfRange(packet.getData(), length - 2, length);
        
        return portFromBytes(portBytes);
	}
	
	private int portFromBytes(byte[] portBytes)
	{
		return ((portBytes[0] << 8) & 0x0000FF00) | (portBytes[1] & 0x000000FF);
	}
    
}
