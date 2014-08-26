package ipxtunnel.client.middleman;

import java.net.DatagramPacket;

public interface PacketModifier
{
    public void modify(DatagramPacket packet);
}
