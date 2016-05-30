package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.Stage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test main menu logic.
 */
public class LevelLogicTest {

    LevelLogic level = new LevelLogic();

    @Test
    public void test() {
        level.processEsc();
        Assert.assertTrue("By default escape should back to menu", level.getEvent() == GameEvent.SWITCH_STAGE);
        Assert.assertTrue("By default escape should back to menu", level.getStageToLoad() == Stage.MAIN_MENU);
    }

}
