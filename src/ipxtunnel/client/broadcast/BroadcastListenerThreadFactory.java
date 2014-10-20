package ipxtunnel.client.broadcast;

import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;

import java.net.DatagramSocket;
import java.net.MulticastSocket;

public class BroadcastListenerThreadFactory
{
    private PacketListenerFactory packetListenerFactory;
    private BroadcastListenerFactory broadcastListenerFactory;
    private BroadcastHandlerFactory broadcastHandlerFactory;
    
    public MiddleManThread construct(DatagramSocket sendsToServer, MulticastSocket receivesBroadcasts)
    {
        PacketListener packetListener = packetListenerFactory.construct(receivesBroadcasts);
        BroadcastHandler broadcastHandler = broadcastHandlerFactory.construct(sendsToServer);
        MiddleMan broadcastListener = broadcastListenerFactory.construct(packetListener, broadcastHandler);
        return new MiddleManThread(broadcastListener);
    }

}
