package ipxtunnel.client.broadcast;

import static ipxtunnel.answers.EndOfThreadTestAnswer.stopThread;
import static ipxtunnel.answers.PacketAnswer.setReceivedPacketTo;
import static ipxtunnel.matchers.ConnectionDetailsMatcher.socketAddressMatching;
import static ipxtunnel.matchers.PacketDataMatcher.packetWithData;
import static ipxtunnel.matchers.PacketDestinationMatcher.packetWithDestination;
import static ipxtunnel.thread.ThreadTest.runThreadAndWaitForDeath;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import ipxtunnel.client.middleman.MiddleManFactory;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketListenerFactory;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.matchers.ConnectionDetailsMatcher;
import ipxtunnel.matchers.PacketDataMatcher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(
        {BroadcastListenerThreadFactory.class,
         PacketListenerFactory.class,
         BroadcastHandlerFactory.class,
         BroadcastListenerFactory.class,
         BroadcastListenerThreadFactory.class,
         MiddleManFactory.class,
         PacketSender.class,
         MulticastSocket.class,
         DatagramSocket.class})
public class BroadcastListenerIntegrationTest
{
    private MiddleManThread broadcastListenerThread;
    
    private BroadcastListenerThreadFactory broadcastListenerThreadFactory = new BroadcastListenerThreadFactory();
    
    @Mock
    private DatagramSocket sendsToServer; 
    
    @Mock
    private MulticastSocket receivesBroadcasts; 
    
    private DatagramPacket broadcastPacket;
    
    @Mock
    private ConnectionDetails serverConnectionDetails;
    private String serverAddressName = "1.1.1.2";
    private InetAddress serverAddress;
    private int serverPort = 1;
    
    @Mock
    private ConnectionDetails broadcastConnectionDetails;
    private String broadcastAddressName = "1.1.1.1";
    private InetAddress broadcastAddress;
    private int broadcastPort = 2;
    
    private byte[] initialData = new byte[]{0x05};
    private byte[] expectedData = new byte[]{0x05, 0x00, 0x01, 0x01, 0x01, 0x02, 0x00, 0x03, 0x00, 0x00};
    private int senderPort = 3;
    
    @Before
    public void setup() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        
        stubSendingSocket();
        stubReceivingSocket();
        stubPacketReception();
    }

    private void stubPacketReception() throws IOException
    {
        broadcastPacket = new DatagramPacket(initialData, initialData.length);
        broadcastPacket.setPort(senderPort);
        broadcastPacket.setAddress(serverAddress);
        doAnswer(setReceivedPacketTo(broadcastPacket)).when(receivesBroadcasts).receive(any(DatagramPacket.class));
    }

    private void stubReceivingSocket() throws UnknownHostException, Exception
    {
        broadcastAddress = InetAddress.getByName(broadcastAddressName);
        when(broadcastConnectionDetails.getAddress()).thenReturn(broadcastAddress);
        when(broadcastConnectionDetails.getPort()).thenReturn(broadcastPort);
        whenNew(MulticastSocket.class).withArguments(argThat(is(socketAddressMatching(broadcastConnectionDetails)))).thenReturn(receivesBroadcasts);
    }

    private void stubSendingSocket() throws UnknownHostException, Exception
    {
        serverAddress = InetAddress.getByName(serverAddressName);
        when(serverConnectionDetails.getAddress()).thenReturn(serverAddress);
        when(serverConnectionDetails.getPort()).thenReturn(serverPort);
        whenNew(DatagramSocket.class).withAnyArguments().thenReturn(sendsToServer);
    }
    
    @Test
    public void broadcastListenerShouldRouteBroadcastPacketToServer() throws InterruptedException, IOException
    {
        broadcastListenerThread = broadcastListenerThreadFactory.construct(serverConnectionDetails, broadcastConnectionDetails);
        doAnswer(stopThread(broadcastListenerThread)).when(sendsToServer).send(any(DatagramPacket.class));
        
        runThreadAndWaitForDeath(broadcastListenerThread);
        
        verify(sendsToServer).send(argThat(is(packetWithDestination(serverAddress, serverPort))));
    }
    
    @Test
    public void broadcastListenerShouldPutMetaDataOnPacketBeforeSending() throws IOException, InterruptedException {
        broadcastListenerThread = broadcastListenerThreadFactory.construct(serverConnectionDetails, broadcastConnectionDetails);
        doAnswer(stopThread(broadcastListenerThread)).when(sendsToServer).send(any(DatagramPacket.class));
        
        runThreadAndWaitForDeath(broadcastListenerThread);
        
        verify(sendsToServer).send(argThat(is(packetWithData(expectedData))));
    }
}
