package ipxtunnel.answers;

import ipxtunnel.client.middleman.MiddleManThread;

import java.net.DatagramPacket;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class EndOfThreadTestAnswer<T> implements Answer<Thread>
{
    private MiddleManThread thread;
    
    public static EndOfThreadTestAnswer<DatagramPacket> stopThread(MiddleManThread thread)
    {
        return new EndOfThreadTestAnswer<DatagramPacket>(thread);
    }
    
    public EndOfThreadTestAnswer(MiddleManThread thread)
    {
        this.thread = thread;
    }
    
    @Override
    public MiddleManThread answer(InvocationOnMock invocation) throws Throwable
    {
        thread.interrupt();
        
        return null;
    }

}
