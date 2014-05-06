package ipxtunnel.client.injectors;

import java.net.DatagramPacket;
import java.util.Arrays;

public class PacketStripper
{

    public void strip(DatagramPacket packet)
    {
        byte[] message = Arrays.copyOf(packet.getData(), packet.getLength() - 9);
        packet.setData(message);
    }
}
