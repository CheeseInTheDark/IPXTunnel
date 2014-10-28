package ipxtunnel.client.broadcast;

import static ipxtunnel.answers.EndOfThreadTestAnswer.stopThread;
import static ipxtunnel.answers.PacketAnswer.setReceivedPacketTo;
import static ipxtunnel.matchers.PacketDestinationMatcher.packetWithDestination;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import ipxtunnel.answers.EndOfThreadTestAnswer;
import ipxtunnel.answers.PacketAnswer;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.matchers.PacketDestinationMatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    private InetAddress serverAddress;
    
    private int serverPort = 123;
    
    @Before
    public void setup() throws IOException
    {
        MockitoAnnotations.initMocks(this);
        
        serverAddress = InetAddress.getByName("1.1.1.1");
        
        broadcastPacket = new DatagramPacket(new byte[0], 0);
        broadcastPacket.setPort(0);
        broadcastPacket.setAddress(serverAddress);
        doAnswer(setReceivedPacketTo(broadcastPacket)).when(receivesBroadcasts).receive(any(DatagramPacket.class));
        
    }
    
    @Test
    public void BroadcastListenerShouldRouteBroadcastPacketToServer() throws InterruptedException, IOException
    {
        broadcastListenerThread = broadcastListenerThreadFactory.construct(sendsToServer, receivesBroadcasts);
        doAnswer(stopThread(broadcastListenerThread)).when(sendsToServer).send(any(DatagramPacket.class));
        
        runBroadcastListenerThread();
        
        verify(sendsToServer).send(argThat(is(packetWithDestination(serverAddress, serverPort))));
    }
    
    private void runBroadcastListenerThread() throws InterruptedException
    {
        broadcastListenerThread.start();
        broadcastListenerThread.waitForDeath();
    }
}
