package com.astro.game.engine.base;

import com.google.common.base.Preconditions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

/**
 * Test of the movable paralax background.
 */
@ContextConfiguration("classpath:config/game-context.xml")
public class ParallaxBackgroundTests {

    @Autowired
    private ParallaxBackground background;

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test
    public void injectPropertiesTest() {
        //given
        Preconditions.checkNotNull(background, "ParallaxBackground is null");

        //then
        Assert.assertTrue("Amount of background should bigger than 0", background.getBackgroundAmount() != 0);
        Assert.assertTrue("Margin should be different than 0", background.getTextureMarginDraw() != 0f);
        Assert.assertTrue("Scale should be different than 0", background.getTextureScale() != 0f);
    }
}
