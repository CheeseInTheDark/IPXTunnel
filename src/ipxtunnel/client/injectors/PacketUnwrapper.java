package ipxtunnel.client.injectors;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

public class PacketUnwrapper
{

    private PacketAddresser addresser = new PacketAddresser();
    private PacketStripper stripper = new PacketStripper();
    
    public void unwrap(DatagramPacket packet) throws UnknownHostException
    {
        addresser.address(packet);
        stripper.strip(packet);
    }

}
