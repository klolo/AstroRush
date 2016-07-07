package com.astro.core.engine.base;

import com.google.common.base.Preconditions;
import common.GdxTestRunner;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;


@ContextConfiguration("classpath:configuration/core-config.xml")
@RunWith(GdxTestRunner.class)
public class GameLogicTest implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Autowired
    private GameLogic gameLogic;


    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test
    public void shouldBeCorrectedInitialized() {
        //given
        Preconditions.checkNotNull(gameLogic, "Game logic should be injected");

        //when
        GameEngine.setApplicationContext(applicationContext);

        //then
        Assert.assertNotNull("stageFactory should be set", gameLogic.stageFactory);
    }

}
