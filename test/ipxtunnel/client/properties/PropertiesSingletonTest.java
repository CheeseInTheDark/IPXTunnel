package ipxtunnel.client.properties;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {PropertiesSingleton.class, PropertiesFactory.class} )
public class PropertiesSingletonTest
{
    @Mock
    private Properties expectedInstance;
    
    @Mock
    private PropertiesFactory propertiesFactory;
    
    private String[] rawArguments = new String[0];
    
    @Before
    public void setup() throws Exception
    {
        whenNew(PropertiesFactory.class).withNoArguments().thenReturn(propertiesFactory);
        when(propertiesFactory.construct(rawArguments)).thenReturn(expectedInstance);
    }
    
    @Test
    public void shouldInitialize()
    {
        PropertiesSingleton.initialize(rawArguments);
        
        Properties instance = PropertiesSingleton.getInstance();
        
        assertThat(instance, is(expectedInstance));
    }
}
