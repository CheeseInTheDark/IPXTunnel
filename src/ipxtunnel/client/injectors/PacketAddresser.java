package ipxtunnel.client.injectors;

import ipxtunnel.common.IPXPacketUnpacker;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

public class PacketAddresser
{

    public PacketAddresser()
    {
    }
    
    public void address(DatagramPacket packet) throws UnknownHostException
    {
        IPXPacketUnpacker unpacker = new IPXPacketUnpacker();
        packet.setAddress(unpacker.extractSenderAddress(packet));
        packet.setPort(unpacker.extractSenderPort(packet));
    }
}
