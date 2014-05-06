package ipxtunnel.client.injectors;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

public class PacketUnwrapper
{

    PacketAddresser addresser;
    PacketStripper stripper;
    
    public PacketUnwrapper(PacketAddresser addresser, PacketStripper stripper)
    {
        this.addresser = addresser;
        this.stripper = stripper;
    }

    public void unwrap(DatagramPacket packet) throws UnknownHostException
    {
        addresser.address(packet);
        stripper.strip(packet);
    }

}
