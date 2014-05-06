package ipxtunnel.client.socketwrappers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class FakeNodeSender
{
    private DatagramSocket socket;
    
    public FakeNodeSender(DatagramSocket socket)
    {
        this.socket = socket;
    }

    public void send(DatagramPacket packet) throws IOException
    {
        socket.send(packet);
    }

}
