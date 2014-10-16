package ipxtunnel.client.tunnel;

import static ipxtunnel.matchers.PacketDataMatcher.packetWithData;
import static ipxtunnel.matchers.PacketDestinationMatcher.packetWithDestination;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import ipxtunnel.answers.EndOfThreadTestAnswer;
import ipxtunnel.answers.PacketAnswer;
import ipxtunnel.client.TunnelListenerThread;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.properties.PropertiesSingleton;
import ipxtunnel.matchers.PacketDataMatcher;
import ipxtunnel.matchers.PacketDestinationMatcher;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

public class TunnelListenerIntegrationTest
{
    @Mock
    private DatagramSocket receivesFromServer;
    
    @Mock
    private NodeDelegate nodeDelegate;
    
    @Mock
    private NodeDelegate anotherNodeDelegate;
    
    @Mock
    private NodeDelegates nodeDelegates;
    
    private InetAddress firstLocalNodeAddress;
    private int firstLocalNodePort;
    
    private InetAddress secondLocalNodeAddress;
    private int secondLocalNodePort;

    private TunnelListenerThreadFactory tunnelListenerThreadFactory = new TunnelListenerThreadFactory();
    
    private String IPXPort = "0";
    private String serverPort = "456";
    private String serverIp = "127.0.0.1";
    
    private String[] rawArguments = {IPXPort, serverPort, serverIp};
    
    private DatagramPacket packetFromFirstDelegate;
    private DatagramPacket packetFromSecondDelegate;
    
    private MiddleManThread underTest;
    
    @Before
    public void setup() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        
        when(nodeDelegates.get(2)).thenReturn(nodeDelegate);
        when(nodeDelegates.get(4)).thenReturn(anotherNodeDelegate);
      
        PropertiesSingleton.initialize(rawArguments);
        
        firstLocalNodeAddress = InetAddress.getByName("1.1.1.1");
        firstLocalNodePort = 1;
        
        secondLocalNodeAddress = InetAddress.getByName("1.1.1.2");
        secondLocalNodePort = 2;
        
        initializePackets();
        
        doAnswer(new PacketAnswer(packetFromFirstDelegate))
        .doAnswer(new PacketAnswer(packetFromSecondDelegate))
        .when(receivesFromServer).receive(any(DatagramPacket.class));
    }
    
    private void stopTestAfterNodeDelegatesAreCalled()
    {
        doAnswer(new EndOfThreadTestAnswer<DatagramPacket>(underTest)).when(anotherNodeDelegate).send(any(DatagramPacket.class));
    }
    
    private void initializePackets() throws UnknownHostException
    {
        byte[] firstData = {0x0F, 0x00, 0x01, 0x01, 0x01, 0x01, 0x00, 0x01, 0x00, 0x02};
        packetFromFirstDelegate = buildPacket(firstData);
        
        byte[] secondData = {0x0F, 0x00, 0x01, 0x01, 0x01, 0x02, 0x00, 0x02, 0x00, 0x04};
        packetFromSecondDelegate = buildPacket(secondData);      
    }
    
    private DatagramPacket buildPacket(byte[] data) throws NumberFormatException, UnknownHostException
    {
        return new DatagramPacket(data, data.length, InetAddress.getByName(serverIp), Integer.parseInt(serverPort));
    }
    
    @Test
    public void shouldRouteIncomingPacketToCorrectNodeDelegate() throws InterruptedException 
    {
        underTest = tunnelListenerThreadFactory.construct(receivesFromServer, nodeDelegates);
        stopTestAfterNodeDelegatesAreCalled();
        
        runTunnelListener();
        
        InOrder inOrder = inOrder(nodeDelegate, anotherNodeDelegate);
        inOrder.verify(nodeDelegate).send(argThat(is(packetWithDestination(firstLocalNodeAddress, firstLocalNodePort))));
        inOrder.verify(anotherNodeDelegate).send(argThat(is(packetWithDestination(secondLocalNodeAddress, secondLocalNodePort))));
    }
    
    @Test
    public void shouldStripPacketbeforeSending() throws InterruptedException
    {
        underTest = tunnelListenerThreadFactory.construct(receivesFromServer, nodeDelegates);
        stopTestAfterNodeDelegatesAreCalled();
        
        runTunnelListener();
        
        InOrder inOrder = inOrder(nodeDelegate, anotherNodeDelegate);
        inOrder.verify(nodeDelegate).send(argThat(is(packetWithData(new byte[]{0x0F}))));
    }
    
    private void runTunnelListener() throws InterruptedException
    {
        underTest.start();
        underTest.waitForDeath();        
    }
}
