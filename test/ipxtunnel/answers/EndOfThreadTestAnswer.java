package ipxtunnel.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class EndOfThreadTestAnswer<T> implements Answer<Thread>
{
    private Thread thread;
    
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
