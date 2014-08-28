package ipxtunnel.client.middleman;

import java.net.DatagramPacket;

public interface PacketHandler
{
    public void modify(DatagramPacket packet);
}
