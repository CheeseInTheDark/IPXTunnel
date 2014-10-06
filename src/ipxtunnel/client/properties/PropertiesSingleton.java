package ipxtunnel.client.properties;

public class PropertiesSingleton
{
    private static PropertiesFactory propertiesFactory = new PropertiesFactory();
    
    private static Properties instance;
    
    public static void initialize(String[] arguments)
    {
        instance = propertiesFactory.construct(arguments);
    }
    
    public static Properties getInstance()
    {
        return instance;
    }
}
