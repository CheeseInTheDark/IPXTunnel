package ipxtunnel.client.tunnel;

import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketSender;

public class TunnelListenerFactory {

    public MiddleMan construct(PacketListener packetListener,
            TunnelHandler tunnelHandler)
    {
        return new MiddleMan(packetListener, tunnelHandler);
    }

}
