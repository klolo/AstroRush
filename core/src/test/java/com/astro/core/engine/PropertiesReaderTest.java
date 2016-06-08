package com.astro.core.engine;

import com.astro.core.storage.PropertiesReader;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PropertiesReader.
 */
public class PropertiesReaderTest {

    @Test
    public void shouldPropertiesNotBeNull() {
        //when
        PropertiesReader.instance.init();

        //then
        Assert.assertNotNull("Properties should not be null", PropertiesReader.getGameProperties());
        Assert.assertNull("Property should be null", PropertiesReader.instance.getProperty("test123"));
    }

    @Test
    public void shouldBePropertyReaded() {
        //when
        PropertiesReader.instance.init();

        //then
        Assert.assertTrue("Window width should be defined", PropertiesReader.instance.getProperty("window.width") != null
                && !PropertiesReader.instance.getProperty("window.width").equals(""));
        Assert.assertTrue("Window height should be defined", PropertiesReader.instance.getProperty("window.width") != null
                && !PropertiesReader.instance.getProperty("window.height").equals(""));
    }

}
