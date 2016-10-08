package com.astro.game.engine;

import com.astro.game.adnotation.GameProperty;
import com.astro.game.adnotation.Msg;
import com.astro.game.adnotation.processor.PropertyInjector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PropertyInjector.
 */
public class PropertyInjectorTest {

    private class ForInjectTestClass {
        @GameProperty("renderer.pixel.per.meter")
        int width = 0;

        @Msg("player.diaLOGGER.start")
        String startMsg = "";
    }

    @Test
    public void shouldInjectProperties() {
        //given
        ForInjectTestClass testObj = new ForInjectTestClass();

        //when
        PropertyInjector.instance.inject(testObj);

        //then
        Assert.assertTrue("Properties should be 0", testObj.width != 0);
        Assert.assertTrue("Message should initialized", !"".equals(testObj.startMsg));
    }

}
