package ipxtunnel.client.properties;

import java.net.InetAddress;

public class ConnectionDetails
{

    private InetAddress address;
    
    private int port;
    
    public ConnectionDetails(InetAddress address, int port)
    {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }

}
