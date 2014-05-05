package ipxtunnel.client.directpackets;

import ipxtunnel.common.IPXPacketInjector;

import java.net.DatagramPacket;

public class DirectPacketInjector
{
    private final byte DIRECT_PACKET_TYPE = 0x01;
    private int destinationPort;
    
    public DirectPacketInjector(int destinationPort)
    {
        this.destinationPort = destinationPort;
    }

    public void inject(DatagramPacket packet)
    {
        IPXPacketInjector injector = new IPXPacketInjector(packet);
        injector.injectSender(packet.getAddress());
        injector.injectSenderPort(packet.getPort());
        injector.injectDestinationPort(destinationPort);
        injector.injectPacketType(DIRECT_PACKET_TYPE);
    }

}
