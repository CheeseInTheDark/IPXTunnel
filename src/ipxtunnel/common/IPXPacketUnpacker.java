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
        return 12321;
    }
    
}
