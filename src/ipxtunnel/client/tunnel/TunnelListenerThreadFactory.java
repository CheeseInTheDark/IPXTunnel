package ipxtunnel.client.tunnel;

import java.net.DatagramSocket;

import ipxtunnel.client.delegates.NodeDelegates;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManFactory;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;

public class TunnelListenerThreadFactory
{
    private MiddleManFactory middleManFactory = new MiddleManFactory();
    private TunnelHandlerFactory tunnelHandlerFactory = new TunnelHandlerFactory();
    private PacketListenerFactory packetListenerFactory = new PacketListenerFactory();
    
    public MiddleManThread construct(DatagramSocket receivesFromServer, NodeDelegates nodeDelegates)
    {
        TunnelHandler tunnelHandler = tunnelHandlerFactory.construct(nodeDelegates);
        PacketListener packetListener = packetListenerFactory .construct(receivesFromServer);
        MiddleMan tunnelListener = middleManFactory.construct(packetListener, tunnelHandler);
        
        return new MiddleManThread(tunnelListener);
    }

}
