package ipxtunnel.client.socketwrappers;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import ipxtunnel.client.socketwrappers.PacketSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.Test;

public class PacketSenderTest
{

    @Test
    public void testPacketSenderSendsPacket() throws IOException
    {
        DatagramSocket serverSocket = mock(DatagramSocket.class);
        PacketSender sender = new PacketSender(serverSocket, InetAddress.getByName("192.168.1.100"), 123);
        DatagramPacket packet = new DatagramPacket(new byte[] {0x12}, 1);
        
        sender.send(packet);
        
        DatagramPacket sentPacket = new DatagramPacket(new byte[] {0x12}, 1);
        sentPacket.setAddress(InetAddress.getByName("192.168.1.100"));
        sentPacket.setPort(123);
        verify(serverSocket).send(refEq(sentPacket, ""));
    }

}
