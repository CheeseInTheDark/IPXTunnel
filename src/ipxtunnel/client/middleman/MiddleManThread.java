package ipxtunnel.client.middleman;

import ipxtunnel.client.IPXTunnelClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.Arrays;

public class MiddleManThread extends Thread implements Runnable 
{
	private boolean finished = false;
	
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
	        try
            {
                middleMan.handleOnePacket();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
	    }
	    while (!interrupted());
	    
	    finished = true;
	    notify();
	}

    public synchronized void waitForDeath() throws InterruptedException
    {
        while (!finished)
        {
            wait();
        }
    }
}
