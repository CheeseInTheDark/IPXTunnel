package ipxtunnel.client.socketwrappers;

import static ipxtunnel.matchers.PacketDataMatcher.packetWithData;
import static ipxtunnel.matchers.PacketDestinationMatcher.packetWithDestination;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.matchers.PacketDataMatcher;
import ipxtunnel.matchers.PacketDestinationMatcher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PacketSenderTest
{
    private int destinationPort = 123;

    @InjectMocks
    private PacketSender sender;
    
    @Mock
    private InetAddress destinationAddress;
    
    
    @Mock
    private DatagramSocket sendingSocket;
    
    private byte[] data = {0x12};
    
    @Before
    public void setup() throws SocketException
    {
        sender = new PacketSender(null, destinationPort);
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testPacketSenderSendsPacket() throws IOException
    {
        DatagramPacket packet = new DatagramPacket(new byte[] {0x12}, 1);
        
        sender.send(packet);
        
        verify(sendingSocket).send(argThat(is(packetWith(data, destinationAddress, destinationPort))));
    }
    
    private Matcher<DatagramPacket> packetWith(byte[] data, InetAddress address, int port)
    {
        return allOf(packetWithData(data), packetWithDestination(destinationAddress, destinationPort));
    }
}
