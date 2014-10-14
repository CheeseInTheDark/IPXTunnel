package ipxtunnel.client.middleman;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

public interface PacketHandler
{
    public void handle(DatagramPacket packet) throws UnknownHostException;
}
