package ipxtunnel.answers;

import java.net.DatagramPacket;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class EndOfThreadTestAnswer<T> implements Answer<Thread>
{
    private Thread thread;
    
    public static EndOfThreadTestAnswer<DatagramPacket> stopThread(Thread thread)
    {
        return new EndOfThreadTestAnswer<DatagramPacket>(thread);
    }
    
    public EndOfThreadTestAnswer(Thread thread)
    {
        this.thread = thread;
    }
    
    @Override
    public Thread answer(InvocationOnMock invocation) throws Throwable
    {
        thread.interrupt();
        return null;
    }

}
