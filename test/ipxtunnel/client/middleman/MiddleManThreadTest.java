package ipxtunnel.client.middleman;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import ipxtunnel.client.middleman.MiddleMan;
import ipxtunnel.client.middleman.MiddleManThread;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MiddleManThreadTest
{
	@Mock
	MiddleMan middleMan;
	
	@InjectMocks
	MiddleManThread thread;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		thread = new MiddleManThread(middleMan);
	}
	
	@Test
	public void testThreadPersists() throws InterruptedException
	{
		startThread();

		assertTrue(thread.isAlive());
		assertFalse(thread.isInterrupted());
	}
	
    @Test
    public void testThreadOnlyExitsOnInterrupt() throws InterruptedException
    {
    	startAndInterruptThread();
        
        assertFalse(thread.isAlive());
    }
    
    @Test
    public void testThreadHandlesOnePacket() throws IOException, InterruptedException
    {
    	startAndInterruptThread();
    	
        verify(middleMan).handleOnePacket();
    }
    
    private void startAndInterruptThread() throws InterruptedException
    {
    	startThread();
    	stopThread();
    }
    
    private void startThread()
    {
    	thread.start();
    }
    
    private void stopThread() throws InterruptedException
    {
        thread.interrupt();
        thread.waitForDeath();    	
    }
}
