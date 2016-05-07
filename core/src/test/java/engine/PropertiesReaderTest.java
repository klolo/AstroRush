package engine;

import com.astro.core.storage.PropertiesReader;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PropertiesReader.
 */
public class PropertiesReaderTest {

    @Test
    public void testReader() {
        PropertiesReader.instance.init();
        Assert.assertNotNull("Properties should not be null", PropertiesReader.instance.getGameProperties());
        Assert.assertNull("Property should be null", PropertiesReader.instance.getProperty("test123"));
    }

    @Test
    public void testReadedProperties() {
        Assert.assertTrue("Window width should be defined", PropertiesReader.instance.getProperty("window.width") != null
                && !PropertiesReader.instance.getProperty("window.width").equals(""));
        Assert.assertTrue("Window height should be defined", PropertiesReader.instance.getProperty("window.width") != null
                && !PropertiesReader.instance.getProperty("window.height").equals(""));
    }

}
