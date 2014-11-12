package ipxtunnel.client.direct;

import java.net.SocketException;

import ipxtunnel.client.injectors.DirectPacketInjector;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

public class DirectPacketHandlerFactory
{
    private PacketSenderFactory packetSenderFactory = new PacketSenderFactory();
    
    public DirectPacketHandler construct(ConnectionDetails serverConnectionDetails, int receivingPort) throws SocketException
    {
        DirectPacketInjector injector = new DirectPacketInjector(receivingPort);
        PacketSender sender = packetSenderFactory.construct(serverConnectionDetails);
        return new DirectPacketHandler(sender, injector);
    }

}
