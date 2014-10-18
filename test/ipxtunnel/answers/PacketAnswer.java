package ipxtunnel.answers;

import java.net.DatagramPacket;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class PacketAnswer implements Answer<Void>
{
    private DatagramPacket packet;
    
    public static PacketAnswer setReceivedPacketTo(DatagramPacket packet)
    {
        return new PacketAnswer(packet);
    }
    
    public PacketAnswer(DatagramPacket packet)
    {
        this.packet = packet;
    }
    
    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable
    {
        DatagramPacket receivedPacket = (DatagramPacket) invocation.getArguments()[0];
        receivedPacket.setPort(packet.getPort());
        receivedPacket.setAddress(packet.getAddress());
        receivedPacket.setData(packet.getData());
        receivedPacket.setLength(packet.getLength());
        return null;
    }
}
