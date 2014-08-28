package ipxtunnel.client.injectors;

import static org.junit.Assert.assertArrayEquals;
import ipxtunnel.client.injectors.DirectPacketInjector;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.Test;

public class DirectPacketInjectorTest
{

    @Test
    public void testInjectorInjectsDirectPacketInformation() throws UnknownHostException
    {
        byte[] message = new byte[] {0x0A};
        DatagramPacket packet = new DatagramPacket(message, message.length);
        packet.setPort(15);
        packet.setAddress(InetAddress.getByName("127.0.0.1"));
        DirectPacketInjector injector = new DirectPacketInjector(14);

        injector.inject(packet);

        byte[] expectedMessage = new byte[] {0x0A, 0x01, 0x7F, 0x00, 0x00, 0x01, 0x00, 0x0F, 0x00, 0x0E};
        byte[] actualMessage = Arrays.copyOf(packet.getData(), packet.getLength());
        assertArrayEquals(expectedMessage, actualMessage);
    }

}
