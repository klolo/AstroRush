package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test main menu logic.
 */
public class MainMenuTest {

    @Test
    public void shouldSwitchGame() {
        //given
        final MainMenu menu = new MainMenu();

        //when
        menu.processEnter();

        //then
        Assert.assertTrue("By default enter should run game", menu.getEvent() == GameEvent.SWITCH_STAGE);
    }

    @Test
    public void shouldGameExit() {
        //given
        final MainMenu menu = new MainMenu();

        //when
        for (int i = 0; i < 30; i++) {
            menu.processArrowDown();
        }
        menu.processEnter();

        //then
        Assert.assertTrue("Last button should on last position - exit", menu.getEvent() == GameEvent.GAME_EXIT);
    }

}
