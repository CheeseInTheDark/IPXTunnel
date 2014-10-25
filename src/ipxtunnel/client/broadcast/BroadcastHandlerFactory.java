package ipxtunnel.client.broadcast;

import java.net.DatagramSocket;

public class BroadcastHandlerFactory
{
    public BroadcastHandler construct(DatagramSocket sendsToServer)
    {
        return new BroadcastHandler();
    }

}
