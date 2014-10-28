package ipxtunnel.client.socketwrappers;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSenderFactory
{

    public PacketSender construct(DatagramSocket sendingSocket, InetAddress destinationAddress, int destinationPort)
    {
        return new PacketSender(sendingSocket, destinationAddress, destinationPort);
    }

}
