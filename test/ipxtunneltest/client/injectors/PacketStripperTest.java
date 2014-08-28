package ipxtunneltest.client.injectors;

import static org.junit.Assert.*;

import java.net.DatagramPacket;

import ipxtunnel.client.injectors.PacketStripper;

import org.junit.Test;

public class PacketStripperTest
{

    @Test
    public void testStripsPackets()
    {
        byte[] message = new byte[10];
        DatagramPacket packet = new DatagramPacket(message, message.length);
        PacketStripper stripper = new PacketStripper();
        
        stripper.strip(packet);
        
        int expectedMessageLength = 1;
        assertEquals(expectedMessageLength, packet.getLength());
    }

}
