package ipxtunneltest.client.middleman;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManThread;

import java.io.IOException;

import org.junit.Test;

public class MiddlemanThreadTest
{

    @Test
    public void testThreadOnlyExitsOnInterrupt() throws InterruptedException
    {
        MiddleMan middleMan = mock(MiddleMan.class);
        MiddleManThread thread = new MiddleManThread(middleMan);
        
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
        MiddleMan middleMan = mock(MiddleMan.class);
        MiddleManThread thread = new MiddleManThread(middleMan);
        
        thread.start();
        thread.interrupt();
        thread.waitForDeath();
        
        verify(middleMan).handleOnePacket();
    }
}
