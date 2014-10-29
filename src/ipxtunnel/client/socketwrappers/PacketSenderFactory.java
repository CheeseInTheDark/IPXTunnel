package ipxtunnel.client.socketwrappers;

import ipxtunnel.client.properties.ConnectionDetails;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSenderFactory
{

    public PacketSender construct(DatagramSocket sendingSocket, ConnectionDetails destination)
    {
        return new PacketSender(sendingSocket, destination.getAddress(), destination.getPort());
    }

}
