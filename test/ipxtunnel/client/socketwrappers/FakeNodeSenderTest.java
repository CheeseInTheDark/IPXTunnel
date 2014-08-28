package ipxtunnel.client.socketwrappers;

import static org.mockito.Mockito.*;
import ipxtunnel.client.socketwrappers.FakeNodeSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.junit.Test;

public class FakeNodeSenderTest
{
    @Test
    public void testFakeNodeSenderSendsPackets() throws IOException
    {
        DatagramPacket packet = new DatagramPacket(new byte[0], 0);
        DatagramSocket socket = mock(DatagramSocket.class);
        FakeNodeSender fakeNode = new FakeNodeSender(socket);
        
        fakeNode.send(packet);
        
        verify(socket).send(packet);
    }
}
