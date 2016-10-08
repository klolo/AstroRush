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
        //given
        final PropertiesReader propertiesReader = PropertiesReader.instance;

        //when
        propertiesReader.init();

        //then
        Assert.assertNotNull("Properties should not be null", PropertiesReader.getGameProperties());
        Assert.assertNull("Property should be null", PropertiesReader.instance.getProperty("test123"));
    }

}
