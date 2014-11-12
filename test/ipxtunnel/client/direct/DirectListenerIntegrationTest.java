package ipxtunnel.client.direct;

import static ipxtunnel.answers.EndOfThreadTestAnswer.stopThread;
import static ipxtunnel.answers.PacketAnswer.setReceivedPacketTo;
import static ipxtunnel.matchers.PacketDestinationMatcher.packetWithDestination;
import static ipxtunnel.thread.ThreadTest.runThreadAndWaitForDeath;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import ipxtunnel.client.middleman.MiddleManThread;
import ipxtunnel.client.properties.ConnectionDetails;
import ipxtunnel.client.socketwrappers.PacketSender;
import ipxtunnel.client.socketwrappers.PacketSenderFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    DirectPacketListenerThreadFactory.class,
    DirectPacketHandlerFactory.class,
    PacketSenderFactory.class,
    DatagramSocket.class,
    PacketSender.class
})
public class DirectListenerIntegrationTest
{

    private DirectPacketListenerThreadFactory directPacketListenerThreadFactory = new DirectPacketListenerThreadFactory();

    @Mock
    private DatagramSocket receivingSocket;
    
    @Mock
    private DatagramSocket sendsToServer;
    
    private ConnectionDetails serverConnectionDetails;
    
    private InetAddress serverAddress;
    private String serverAddressName = "1.1.1.1";
    private int serverPort = 123;
    
    private DatagramPacket directPacket;
    private byte[] initialData = new byte[] {0x0A};
    private int senderPort = 5;
    private InetAddress senderAddress;
    private String senderAddressName = "1.1.1.2";
    
    @Before
    public void setup() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        
        serverAddress = InetAddress.getByName(serverAddressName);
        serverConnectionDetails = new ConnectionDetails(serverAddress, serverPort);
        
        senderAddress = InetAddress.getByName(senderAddressName);
        directPacket = new DatagramPacket(initialData, initialData.length);
        directPacket.setPort(senderPort);
        directPacket.setAddress(senderAddress);
        
        whenNew(DatagramSocket.class).withNoArguments().thenReturn(sendsToServer);
        
        doAnswer(setReceivedPacketTo(directPacket)).when(receivingSocket).receive(any(DatagramPacket.class));
    }
    
    @Test
    public void shouldRoutePacketsToServer() throws IOException, InterruptedException
    {
        MiddleManThread directPacketListenerThread = directPacketListenerThreadFactory.construct(receivingSocket, serverConnectionDetails);
        doAnswer(stopThread(directPacketListenerThread)).when(sendsToServer).send(any(DatagramPacket.class));
        
        runThreadAndWaitForDeath(directPacketListenerThread);
        
        verify(sendsToServer).send(argThat(is(packetWithDestination(serverAddress, serverPort))));
    }
}
