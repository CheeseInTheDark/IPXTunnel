package ipxtunnel.thread;

import ipxtunnel.client.middleman.MiddleManThread;

public class ThreadTest
{
    public static void runOneCycle(MiddleManThread thread) throws InterruptedException
    {
        thread.start();
        thread.interrupt();
        thread.waitForDeath();
    }
    
    public static void runThreadAndWaitForDeath(MiddleManThread thread) throws InterruptedException
    {
        thread.start();
        thread.waitForDeath();
    }
}
