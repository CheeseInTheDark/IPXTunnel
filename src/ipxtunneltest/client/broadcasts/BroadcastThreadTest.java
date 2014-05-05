package ipxtunneltest.client.broadcasts;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import ipxtunnel.client.broadcasts.BroadcastMiddleMan;
import ipxtunnel.client.broadcasts.BroadcastThread;

import org.junit.Test;

public class BroadcastThreadTest
{

    @Test
    public void testThreadOnlyExitsOnInterrupt() throws InterruptedException
    {
        BroadcastMiddleMan middleMan = mock(BroadcastMiddleMan.class);
        BroadcastThread thread = new BroadcastThread(middleMan);
        
        thread.start();
        
        assertTrue(thread.isAlive());
        assertFalse(thread.isInterrupted());
        
        thread.interrupt();
        thread.waitForDeath();
        
        assertFalse(thread.isAlive());
    }
    
    @Test
    public void testThreadHandlesOnePacket() throws IOException, InterruptedException
    {
        BroadcastMiddleMan middleMan = mock(BroadcastMiddleMan.class);
        BroadcastThread thread = new BroadcastThread(middleMan);
        
        thread.start();
        thread.interrupt();
        thread.waitForDeath();
        
        verify(middleMan).handleOnePacket();
    }
}
