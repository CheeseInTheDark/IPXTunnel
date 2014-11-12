package ipxtunnel.client.direct;

import static ipxtunnel.thread.ThreadTest.runOneCycle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManFactory;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListener;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;
import ipxtunnel.thread.ThreadTest;

import java.io.IOException;
import java.net.DatagramSocket;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DirectPacketListenerThreadFactoryTest
{
    @InjectMocks
    private DirectPacketListenerThreadFactory underTest; 
    
    @Mock
    private DatagramSocket receivingSocket;
    private int receivingPort = 4;
    
    @Mock
    private ConnectionDetails serverConnectionDetails;
    
    @Mock
    private PacketListenerFactory packetListenerFactory;
    
    @Mock
    private PacketListener packetListener;

    @Mock
    private DirectPacketHandler directPacketHandler;
    
    @Mock
    private DirectPacketHandlerFactory directPacketHandlerFactory;

    @Mock
    private MiddleManFactory middleManFactory;
    
    @Mock
    private MiddleMan directPacketMiddleMan;
    
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        
        when(receivingSocket.getLocalPort()).thenReturn(receivingPort);
    }
    
    @Test
    public void shouldCreateDirectPacketListenerThread() throws InterruptedException, IOException
    {
        when(packetListenerFactory.construct(receivingSocket)).thenReturn(packetListener);
        when(directPacketHandlerFactory.construct(serverConnectionDetails, receivingPort)).thenReturn(directPacketHandler);
        when(middleManFactory.construct(packetListener, directPacketHandler)).thenReturn(directPacketMiddleMan);
        
        MiddleManThread thread = underTest.construct(receivingSocket, serverConnectionDetails);
        
        runOneCycle(thread);

        verify(directPacketMiddleMan).handleOnePacket();
    }
}
