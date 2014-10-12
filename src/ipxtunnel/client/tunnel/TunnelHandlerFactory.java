package ipxtunnel.client.tunnel;

public class TunnelHandlerFactory
{

    public TunnelHandler construct(NodeDelegates nodeDelegates)
    {
        return new TunnelHandler(nodeDelegates);
    }

}
