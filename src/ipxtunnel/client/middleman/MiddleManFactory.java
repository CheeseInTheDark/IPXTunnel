package ipxtunnel.client.middleman;

import ipxtunnel.client.socketwrappers.PacketListener;

public class MiddleManFactory
{

    public MiddleMan construct(PacketListener packetListener,
            PacketHandler handler)
    {
        return new MiddleMan(packetListener, handler);
    }

}
