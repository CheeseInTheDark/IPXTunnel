package ipxtunnel.client.tunnel;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ipxtunnel.answers.PacketAnswer;
import ipxtunnel.client.injectors.DirectPacketInjector;
import ipxtunnel.client.injectors.PacketUnwrapper;
import ipxtunnel.common.IPXPacketUnpacker;
import ipxtunnel.matchers.PacketMatcher;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TunnelHandlerTest
{
    @InjectMocks
    private TunnelHandler tunnelHandler;
    
    @Mock
    private NodeDelegates nodeDelegates;
    
    @Mock
    private NodeDelegate nodeDelegate;
    
    @Mock
    private PacketUnwrapper unwrapper;
    
    @Mock
    private IPXPacketUnpacker unpacker;
    
    private DatagramPacket packetFromPortTwo = new DatagramPacket(new byte[]{0x00, 0x01, 0x01, 0x01, 0x01, 0x00, 0x02, 0x00, 0x01}, 9);
    
    @Before
    public void setup()
    {
        tunnelHandler = new TunnelHandler(nodeDelegates);
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldRoutePacketToCorrectNodeDelegate() throws UnknownHostException
    {
        when(unpacker.extractSenderPort(packetFromPortTwo)).thenReturn(2);
        when(nodeDelegates.get(2)).thenReturn(nodeDelegate);
        
        tunnelHandler.handle(packetFromPortTwo);
        
        verify(nodeDelegate).send(argThat(is(packetFromPortTwo)));
    }
    
    @Test
    public void shouldStripPacketMetadataBeforeSending() throws UnknownHostException
    {
        when(unpacker.extractSenderPort(packetFromPortTwo)).thenReturn(2);
        when(nodeDelegates.get(2)).thenReturn(nodeDelegate);
        
        tunnelHandler.handle(packetFromPortTwo);
        
        InOrder inOrder = inOrder(unwrapper, nodeDelegate);
        inOrder.verify(unwrapper).unwrap(packetFromPortTwo);
        inOrder.verify(nodeDelegate).send(packetFromPortTwo);
    }
}
