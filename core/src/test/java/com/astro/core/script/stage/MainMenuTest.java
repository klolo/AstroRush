package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEngine;
import com.astro.core.engine.base.GameEvent;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test main menu logic.
 */
@ContextConfiguration("classpath:configuration/core-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MainMenuTest implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Test
    public void shouldSwitchGame() {
        //given
        GameEngine.setApplicationContext(applicationContext);
        final MainMenu menu = new MainMenu();
        menu.init();

        //when
        menu.processEnter();

        //then
        Assert.assertTrue("By default enter should run game", menu.getEvent() == GameEvent.SWITCH_STAGE);
    }

    @Test
    public void shouldGameExit() {
        //given
        GameEngine.setApplicationContext(applicationContext);
        final MainMenu menu = new MainMenu();
        menu.init();

        //when
        for (int i = 0; i < 30; i++) {
            menu.processArrowDown();
        }
        menu.processEnter();

        //then
        Assert.assertTrue("Last button should on last position - exit", menu.getEvent() == GameEvent.GAME_EXIT);
    }

}
