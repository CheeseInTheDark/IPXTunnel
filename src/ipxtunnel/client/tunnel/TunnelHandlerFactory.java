package ipxtunnel.client.tunnel;

import ipxtunnel.client.delegates.NodeDelegates;

public class TunnelHandlerFactory
{

    public TunnelHandler construct(NodeDelegates nodeDelegates)
    {
        return new TunnelHandler(nodeDelegates);
    }

}
