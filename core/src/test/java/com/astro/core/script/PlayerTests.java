package com.astro.core.script;

import common.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests player script.
 */
@RunWith(GdxTestRunner.class)
public class PlayerTests {

    private Player player;

    @Before
    public void initPlayer() {
        player = new Player();
    }

    @Test
    public void initTest() {
        Assert.assertNotNull("Collision processor should be created", player.getCollisionProcessor());
        Assert.assertNotNull("Collision processor should be created", player.getWatchers());
        Assert.assertTrue("On start player should not have points", player.getPoints() == 0);
        player.addPoints(100);

        Assert.assertTrue("Points should sum", player.getPoints() == 100);
    }

    @Test
    public void liveTest() {
        player.decreaseLive(1000);
        Assert.assertNotNull("Player should be dead", player.isDead());

        player.addLive(player.getStartLiveAmount() + 100);
        Assert.assertNotNull("Player should not have more point than max", player.getLiveAmount() == player.getStartLiveAmount());
    }
}
