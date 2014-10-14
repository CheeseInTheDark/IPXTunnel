package ipxtunnel.matchers;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class PacketMatcher<T> extends TypeSafeMatcher<T>
{
    private InetAddress address;
    private int port;
    
    public PacketMatcher(InetAddress address, int port)
    {
        this.address = address;
        this.port = port;
    }
    
    public static Matcher<DatagramPacket> packetWithDestination(InetAddress address, int port)
    {
        return new PacketMatcher<DatagramPacket>(address, port);
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText(" a DatagramPacket with an address of ");
        description.appendValue(address.getHostName());
        description.appendText(" and a port of ");
        description.appendValue(port);
    }

    @Override
    protected boolean matchesSafely(T item)
    {
        DatagramPacket packet = (DatagramPacket) item; 
        return packet.getAddress().equals(address) && packet.getPort() == port;
    }

}
