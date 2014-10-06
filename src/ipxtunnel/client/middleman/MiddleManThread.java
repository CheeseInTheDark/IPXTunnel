package ipxtunnel.client.middleman;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;

public class MiddleManThread extends Thread implements Runnable 
{
	public MiddleManThread(MulticastSocket broadcastSocket, 
			DatagramSocket tunnelSocket)
	{
	}

	private MiddleMan middleMan;
	
	public MiddleManThread(MiddleMan middleMan)
    {
	    this.middleMan = middleMan;
    }

	@Override
	public synchronized void run()
	{
	    do
	    {
	        handlePacket();
	    } 
	    while (!interrupted());
	    
	    notify();
	}
	
	private void handlePacket()
	{
        try
        {
            middleMan.handleOnePacket();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}

    public synchronized void waitForDeath() throws InterruptedException
    {
        while (isAlive())
        {
            wait();
        }
    }
}
