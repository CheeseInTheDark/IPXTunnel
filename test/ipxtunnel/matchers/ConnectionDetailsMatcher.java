package ipxtunnel.matchers;

import ipxtunnel.client.properties.ConnectionDetails;

import java.net.InetSocketAddress;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ConnectionDetailsMatcher extends TypeSafeMatcher<InetSocketAddress>
{
    private ConnectionDetails toMatch;
    
    public ConnectionDetailsMatcher(ConnectionDetails toMatch)
    {
        this.toMatch = toMatch;
    }
    
    public static ConnectionDetailsMatcher socketAddressMatching(ConnectionDetails toMatch)
    {
        return new ConnectionDetailsMatcher(toMatch);
    }
    
    @Override
    public void describeTo(Description description)
    {
        description.appendText(" an InetSocketAddress with an address of ");
        description.appendValue(toMatch.getAddress());
        description.appendText(" and a port of ");
        description.appendValue(toMatch.getPort());
    }

    @Override
    protected boolean matchesSafely(InetSocketAddress item)
    {
        return item.getAddress().equals(toMatch.getAddress()) &&
               item.getPort() == toMatch.getPort();
    }
}
