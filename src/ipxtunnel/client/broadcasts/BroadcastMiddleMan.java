package ipxtunnel.client.broadcasts;

import java.io.IOException;
import java.net.DatagramPacket;

public class BroadcastMiddleMan
{
    BroadcastInjector injector;
    BroadcastToServerSender sender;
    BroadcastReceiver receiver;
    
    public BroadcastMiddleMan(BroadcastToServerSender sender, BroadcastInjector injector, BroadcastReceiver receiver)
    {
        this.receiver = receiver;
        this.injector = injector;
        this.sender = sender;
    }

    public void handleOnePacket() throws IOException
    {
        DatagramPacket packet = receiver.listen();
        injector.inject(packet);
        sender.send(packet);
    }
}
