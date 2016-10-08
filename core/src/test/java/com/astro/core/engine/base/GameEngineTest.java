package com.astro.core.engine.base;

import com.google.common.base.Preconditions;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;


@ContextConfiguration("classpath:configuration/core-config.xml")
public class GameEngineTest implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Autowired
    private GameEngine gameEngine;


    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailedWithoutApplicationContext() {
        //given
        Preconditions.checkNotNull(gameEngine, "Game engine should be injected");

        //when
        GameEngine.setApplicationContext(null);

        //then
        gameEngine.create();
    }

    @Test
    public void shouldBeCorrectedInitialized() {
        //given
        Preconditions.checkNotNull(gameEngine, "Game engine should be injected");

        //when
        GameEngine.setApplicationContext(applicationContext);

        //then
        Assert.assertNotNull("screen should be set", gameEngine.physicsEngine);
        Assert.assertTrue("screen should be set", gameEngine.timeStep != 0);
    }

}
