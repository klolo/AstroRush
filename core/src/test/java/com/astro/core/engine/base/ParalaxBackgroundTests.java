package com.astro.core.engine.base;

import common.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test of the movable paralax background.
 */
@RunWith(GdxTestRunner.class)
public class ParalaxBackgroundTests {

    @Test
    public void injectPropertiesTest() {
        //given
        final ParalaxBackground background = new ParalaxBackground();

        //then
        Assert.assertTrue("Texture file name should be loaded", !"".equals(background.getTEXTURE_FILE()));
        Assert.assertTrue("Amount of background should bigger than 0", background.getBACKGROUND_AMOUNT() != 0);
        Assert.assertTrue("Margin should be different than 0", background.getTEXTURE_MARGIN_DRAW() != 0f);
        Assert.assertTrue("Scale should be different than 0", background.getTEXTURE_SCALE() != 0f);
    }
}
