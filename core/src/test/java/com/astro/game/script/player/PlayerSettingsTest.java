package com.astro.game.script.player;

import com.google.common.base.Preconditions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/game-context.xml")
public class PlayerSettingsTest {

    @Autowired
    final PlayerSettings playerSettings = new PlayerSettings();

    @Test
    public void shouldBeInit() {
        //given
        Preconditions.checkNotNull(playerSettings, "PlayerSettings is not switchStage");

        //then
        Assert.assertTrue("pixelPerMeter should be loaded", playerSettings.pixelPerMeter != 0);
        Assert.assertTrue("inactiveMsgTime should be loaded", playerSettings.inactiveMsgTime != 0);
        Assert.assertTrue("interactWithObjectTime should be loaded", playerSettings.interactWithObjectTime != 0);
        Assert.assertTrue("maxYPosition should be loaded", playerSettings.maxYPosition != 0);
        Assert.assertTrue("maxYVelocity should be loaded", playerSettings.maxYVelocity != 0);
    }

}
