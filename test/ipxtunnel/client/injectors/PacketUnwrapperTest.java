package ipxtunnel.client.injectors;

import static org.mockito.Mockito.verify;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PacketUnwrapperTest
{
	@Mock
    private PacketAddresser addresser;
	
	@Mock
    private PacketStripper stripper;
	
	@InjectMocks
    private PacketUnwrapper unwrapper;
	
    private DatagramPacket packet;

    @Before
    public void setUp()
    {
    	MockitoAnnotations.initMocks(this);
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
