package ipxtunnel.client.socketwrappers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class PacketSender
{
    private DatagramSocket socket;
    private InetAddress destinationAddress;
    private int destinationPort;
    
    public PacketSender(InetAddress destinationAddress, int destinationPort) throws SocketException
    {
        this.socket = new DatagramSocket();
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
