package ipxtunnel.client.socketwrappers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerBoundPacketSender
{
    private DatagramSocket serverSocket;
    private InetAddress serverAddress;
    private int serverPort;
    
    public ServerBoundPacketSender(DatagramSocket serverSocket, InetAddress serverAddress, int serverPort)
    {
        this.serverSocket = serverSocket;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void send(DatagramPacket packet) throws IOException
    {
        packet.setAddress(serverAddress);
        packet.setPort(serverPort);
        serverSocket.send(packet);
    }

}
