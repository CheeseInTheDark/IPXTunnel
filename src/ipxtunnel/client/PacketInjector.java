package ipxtunnel.client;

import java.net.DatagramPacket;

public interface PacketInjector
{
    public void inject(DatagramPacket packet);
}
