package ipxtunnel.client.socketwrappers;

import java.net.DatagramSocket;

public class PacketListenerFactory
{

    public PacketListener construct(DatagramSocket receivesFromServer)
    {
        return new PacketListener(receivesFromServer);
    }

}
