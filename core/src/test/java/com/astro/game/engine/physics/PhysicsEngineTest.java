package com.astro.game.engine.physics;

import com.google.common.base.Preconditions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/game-context.xml")
public class PhysicsEngineTest {

    @Autowired
    private PhysicsEngine physicsEngine;

    @Test
    public void shouldBeSettingsInjected() {
        //given
        Preconditions.checkNotNull(physicsEngine, "Physics engine is not injected");

        //then
        Assert.assertNotNull("Settings should be injected", physicsEngine.settings);
        Assert.assertTrue("Settings should be set", physicsEngine.settings.getPixelPerMeter() != 0);
        Assert.assertTrue("Settings should be set", physicsEngine.settings.getTimeStep() != 0);
        Assert.assertTrue("Settings should be set", physicsEngine.settings.getGravity() != 0);
    }

}
