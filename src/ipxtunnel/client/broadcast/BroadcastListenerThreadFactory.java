package ipxtunnel.client.broadcast;

import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManFactory;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class BroadcastListenerThreadFactory
{
    private BroadcastListenerFactory broadcastListenerFactory = new BroadcastListenerFactory();
    private MiddleManFactory middleManFactory = new MiddleManFactory();
    private BroadcastHandlerFactory broadcastHandlerFactory = new BroadcastHandlerFactory();
    
    public MiddleManThread construct(ConnectionDetails serverConnectionDetails, ConnectionDetails broadcastConnectionDetails) throws IOException
    {
        PacketListener packetListener = broadcastListenerFactory.construct(broadcastConnectionDetails);
        PacketHandler broadcastHandler = broadcastHandlerFactory.construct(serverConnectionDetails);
        MiddleMan broadcastListener = middleManFactory.construct(packetListener, broadcastHandler);
        
        return new MiddleManThread(broadcastListener);
    }

}
