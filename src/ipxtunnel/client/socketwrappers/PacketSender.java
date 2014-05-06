package ipxtunnel.client.socketwrappers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacketSender
{
    private DatagramSocket socket;
    private InetAddress destinationAddress;
    private int destinationPort;
    
    public PacketSender(DatagramSocket socket, InetAddress destinationAddress, int destinationPort)
    {
        this.socket = socket;
        this.destinationAddress = destinationAddress;
        this.destinationPort = destinationPort;
    }

    public void send(DatagramPacket packet) throws IOException
    {
        packet.setAddress(destinationAddress);
        packet.setPort(destinationPort);
        socket.send(packet);
    }

}
