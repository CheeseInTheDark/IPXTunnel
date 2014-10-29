package ipxtunnel.client.broadcast;

import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManFactory;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;

import java.net.DatagramSocket;
import java.net.MulticastSocket;

public class BroadcastListenerThreadFactory
{
    private PacketListenerFactory packetListenerFactory = new PacketListenerFactory();
    private MiddleManFactory middleManFactory = new MiddleManFactory();
    private BroadcastHandlerFactory broadcastHandlerFactory = new BroadcastHandlerFactory();
    
    public MiddleManThread construct(ConnectionDetails serverConnectionDetails, DatagramSocket sendsToServer, MulticastSocket receivesBroadcasts)
    {
        PacketListener packetListener = packetListenerFactory.construct(receivesBroadcasts);
        PacketHandler broadcastHandler = broadcastHandlerFactory.construct(serverConnectionDetails, sendsToServer);
        MiddleMan broadcastListener = middleManFactory.construct(packetListener, broadcastHandler);
        
        return new MiddleManThread(broadcastListener);
    }

}
