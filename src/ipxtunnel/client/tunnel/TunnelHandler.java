package ipxtunnel.client.tunnel;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

import ipxtunnel.client.delegates.NodeDelegates;
import ipxtunnel.client.injectors.PacketUnwrapper;
import ipxtunnel.client.middleman.PacketHandler;
import ipxtunnel.common.IPXPacketUnpacker;

public class TunnelHandler implements PacketHandler
{
    private PacketUnwrapper unwrapper = new PacketUnwrapper();
    private IPXPacketUnpacker unpacker = new IPXPacketUnpacker();
    private NodeDelegates nodeDelegates;
    
    public TunnelHandler(NodeDelegates nodeDelegates)
    {
        this.nodeDelegates = nodeDelegates;
    }

    @Override
    public void handle(DatagramPacket packet) throws UnknownHostException
    {
        int senderPort = unpacker.extractSenderPort(packet);
        unwrapper.unwrap(packet);
        
        nodeDelegates.get(senderPort).send(packet);
    }
}
