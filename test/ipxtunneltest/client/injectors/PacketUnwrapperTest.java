package ipxtunneltest.client.injectors;

import static org.junit.Assert.*;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

import ipxtunnel.client.injectors.PacketAddresser;
import ipxtunnel.client.injectors.PacketStripper;
import ipxtunnel.client.injectors.PacketUnwrapper;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
public class PacketUnwrapperTest
{
    private PacketAddresser addresser;
    private PacketStripper stripper;
    private PacketUnwrapper unwrapper;
    private DatagramPacket packet;

    @Before
    public void setUp()
    {
        addresser = mock(PacketAddresser.class);
        stripper = mock(PacketStripper.class);    
        unwrapper = new PacketUnwrapper(addresser, stripper);
        packet = new DatagramPacket(new byte[9], 9);
    }

    @Test
    public void testPacketUnwrapperAddressesPacket() throws UnknownHostException
    {
        unwrapper.unwrap(packet);
        
        verify(addresser).address(packet);
    }
    
    @Test
    public void testPacketUnwrapperStripsPacket() throws UnknownHostException
    {
        unwrapper.unwrap(packet);
        
        verify(stripper).strip(packet);
    }

}
