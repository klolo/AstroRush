package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test main menu logic.
 */
public class MainMenuTest {

    MainMenu menu = new MainMenu();

    @Test
    public void test() {
        menu.processEnter();
        Assert.assertTrue("By default enter should run game", menu.getEvent() == GameEvent.SWITCH_STAGE);

        for (int i = 0; i < 30; i++)
            menu.processArrowDown();
        menu.processEnter();

        Assert.assertTrue("Last button should on last position - exit", menu.getEvent() == GameEvent.GAME_EXIT);
    }

}
