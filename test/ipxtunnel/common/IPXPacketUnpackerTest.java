package ipxtunnel.common;

import static org.junit.Assert.*;
import ipxtunnel.common.IPXPacketUnpacker;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class IPXPacketUnpackerTest
{
    private IPXPacketUnpacker unpacker;
    
    private final byte[] localMessage = {0x11, 0x00, 0x7F, 0x00, 0x00, 0x01, 0x30, 0x21, 0x00, 0x0F};
    private final byte[] remoteMessage = {0x11, 0x00, (byte) 0xC0, (byte) 0xA8, 0x01, 0x66, 0x30, 0x21, 0x00, 0x0F};
    
    private InetAddress localHostAddress;
    private InetAddress remoteAddress;
    
    @Before
    public void setUp() throws UnknownHostException
    {
        localHostAddress = InetAddress.getByName("127.0.0.1");
        remoteAddress = InetAddress.getByName("192.168.1.102");
        unpacker = new IPXPacketUnpacker();
    }

    @Test
    public void testIPXPacketUnpackerReturnsSenderAddress() throws UnknownHostException
    {
        DatagramPacket packet = createPacket(localMessage);
        InetAddress sender = unpacker.extractSenderAddress(packet);
        assertEquals(localHostAddress, sender);
        
        packet = createPacket(remoteMessage);
        sender = unpacker.extractSenderAddress(packet);
        assertEquals(remoteAddress, sender);
    }

    @Test
    public void testIPXPacketUnpackerReturnsSenderPort()
    {
        DatagramPacket packet = createPacket(localMessage);
        
        int port = unpacker.extractSenderPort(packet);
        
        int expectedPort = 12321;
        assertEquals(expectedPort, port);
    }
    
    @Test
    public void testIPXPacketUnpackerReturnsDestinationPort()
    {
    	DatagramPacket packet = createPacket(localMessage);
    	
    	int port = unpacker.extractDestinationPort(packet);
    	
    	int expectedPort = 15;
    	assertEquals(expectedPort, port);
    }
    
    private DatagramPacket createPacket(byte[] message)
    {
        return new DatagramPacket(message, message.length);
    }
}
