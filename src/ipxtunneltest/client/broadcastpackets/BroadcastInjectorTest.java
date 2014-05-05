package ipxtunneltest.client.broadcastpackets;

import ipxtunnel.client.broadcastpackets.BroadcastInjector;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

public class BroadcastInjectorTest
{
    @Test
    public void testBroadcastInjectorInjectsBroadcastInformation() throws UnknownHostException
    {
        byte[] message = new byte[] {0x0A};
        DatagramPacket packet = new DatagramPacket(message, message.length);
        packet.setPort(15);
        packet.setAddress(InetAddress.getByName("127.0.0.1"));
        BroadcastInjector injector = new BroadcastInjector();
        
        injector.inject(packet);
        
        byte[] expectedMessage = new byte[] {0x0A, 0x00, 0x7F, 0x00, 0x00, 0x01, 0x00, 0x0F, 0x00, 0x00};
        byte[] actualMessage = Arrays.copyOf(packet.getData(), packet.getLength());
        assertArrayEquals(expectedMessage, actualMessage);
    }
}
