package ipxtunnel.client.broadcast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListener;

public class BroadcastListenerFactory
{

    public PacketListener construct(ConnectionDetails connectionDetails) throws IOException
    {
        InetSocketAddress address = new InetSocketAddress(connectionDetails.getAddress(), connectionDetails.getPort());
        MulticastSocket broadcastSocket = new MulticastSocket(address);
        
        return new PacketListener(broadcastSocket);
    }

}
