package com.astro.game.script.stage;

import com.astro.game.engine.base.GameEvent;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/game-context.xml")
public class MainMenuTest implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Test
    public void shouldSwitchGame() {
        //given
        final MainMenu menu = applicationContext.getBean(MainMenu.class);
        menu.init();

        //when
        menu.processEnter();

        //then
        Assert.assertTrue("By default enter should run game", menu.getEvent() == GameEvent.SWITCH_STAGE);
    }

    @Test
    public void shouldGameExit() {
        //given
        final MainMenu menu = applicationContext.getBean(MainMenu.class);
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
