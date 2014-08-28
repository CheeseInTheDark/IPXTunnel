package ipxtunnel.client.tunnel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.DatagramPacket;

import ipxtunnel.client.injectors.PacketUnwrapper;
import ipxtunnel.client.socketwrappers.FakeNodeSender;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.tunnel.TunnelListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TunnelListenerTest
{
    @Mock
    private PacketListener receiver;
    
    @Mock
    private FakeNodeSender sender;
    
    @Mock
    private PacketUnwrapper unwrapper;
    
    @InjectMocks
    private TunnelListener listener;
    
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testTunnelReceivesPacket() throws IOException
    {
        listener.handleOnePacket();
        
        verify(receiver).listen();
    }
    
    @Test
    public void testTunnelUnwrapsPacket() throws IOException
    {
        byte[] message = {0x00, 0x7F, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x02};
        DatagramPacket packet = new DatagramPacket(message, message.length);
        when(receiver.listen()).thenReturn(packet);
        
        listener.handleOnePacket();
        
        verify(unwrapper).unwrap(packet);
    }

    @Test
    public void testTunnelSendsPacket() throws IOException
    {
        byte[] message = {0x00, 0x7F, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x02};
        DatagramPacket packet = new DatagramPacket(message, message.length);
        when(receiver.listen()).thenReturn(packet);
        
        listener.handleOnePacket();
        
        verify(sender).send(packet);
    }
}
