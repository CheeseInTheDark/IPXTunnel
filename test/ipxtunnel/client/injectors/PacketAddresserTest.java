package ipxtunnel.client.injectors;

import static org.junit.Assert.*;
import ipxtunnel.client.injectors.PacketAddresser;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class PacketAddresserTest
{
    private PacketAddresser addresser;
    private DatagramPacket packet;
    
    @Before
    public void setUp()
    {
        byte[] message = {0x00, 0x7F, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x02};
        packet = new DatagramPacket(message, message.length);
        addresser = new PacketAddresser();
    }
    
    @Test
    public void testAddresserSetsDestinationAddress() throws UnknownHostException
    {
        addresser.address(packet);
        
        InetAddress expectedAddress = InetAddress.getByName("127.0.0.1");
        assertEquals(expectedAddress, packet.getAddress());
    }
    
    @Test
    public void testAddresserSetsDestinationPort() throws UnknownHostException
    {
        addresser.address(packet);
        
        int expectedPort = 1;
        assertEquals(expectedPort, packet.getPort());       
    }
}
