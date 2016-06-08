package com.astro.core.engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.Msg;
import com.astro.core.adnotation.processor.PropertyInjector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PropertyInjector.
 */
public class PropertyInjectorTest {

    private class ForInjectTestClass {
        @GameProperty("window.width")
        int width = 0;

        @Msg("player.dialog.start")
        String startMsg = "";
    }

    @Test
    public void shouldInjectProperties() {
        //given
        ForInjectTestClass testObj = new ForInjectTestClass();

        //when
        PropertyInjector.instance.inject(testObj);

        //then
        Assert.assertTrue("Properties should not be 0", testObj.width != 0);
        Assert.assertTrue("Message should initialized", !"".equals(testObj.startMsg));
    }

}
