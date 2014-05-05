package ipxtunnel.client.broadcastpackets;

import ipxtunnel.common.IPXPacketInjector;

import java.net.DatagramPacket;

public class BroadcastInjector
{
    private final byte BROADCAST_PACKET_TYPE = 0x00;
    
    public BroadcastInjector()
    {
    }
    
    public void inject(DatagramPacket packet)
    {
        IPXPacketInjector injector = new IPXPacketInjector(packet);
        injector.injectSender(packet.getAddress());
        injector.injectSenderPort(packet.getPort());
        injector.injectPacketType(BROADCAST_PACKET_TYPE);
    }
}
