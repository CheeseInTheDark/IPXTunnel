package ipxtunnel.client.direct;

import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManFactory;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;

import java.net.DatagramSocket;
import java.net.SocketException;

public class DirectPacketListenerThreadFactory
{
    private PacketListenerFactory packetListenerFactory = new PacketListenerFactory();
    private DirectPacketHandlerFactory directPacketHandlerFactory = new DirectPacketHandlerFactory();
    private MiddleManFactory middleManFactory = new MiddleManFactory();
    
    public MiddleManThread construct(DatagramSocket receivingSocket, ConnectionDetails serverConnectionDetails) throws SocketException
    {
        PacketListener packetListener = packetListenerFactory.construct(receivingSocket);
        PacketHandler packetHandler = directPacketHandlerFactory.construct(serverConnectionDetails);
        MiddleMan middleMan = middleManFactory.construct(packetListener, packetHandler);
        
        return new MiddleManThread(middleMan);
    }

}
