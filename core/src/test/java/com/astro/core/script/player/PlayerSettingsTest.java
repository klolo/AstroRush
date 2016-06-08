package com.astro.core.script.player;

import org.junit.Assert;
import org.junit.Test;

public class PlayerSettingsTest {

    @Test
    public void shouldBeInit() {
        //given
        final PlayerSettings playerSettings = new PlayerSettings();

        //then
        Assert.assertTrue("PPM should be loaded", playerSettings.PIXEL_PER_METER != 0);
    }

}
