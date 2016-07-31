package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.Stage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test main menu logic.
 */
public class LevelLogicTest {

    @Test
    public void shouldSwitchToPreviousStage() {
        //given
        final LevelLogic level = new LevelLogic();

        //when
        level.processEsc();

        //then
        Assert.assertTrue("By default escape should back to menu", level.getEvent() == GameEvent.SWITCH_STAGE);
        Assert.assertTrue("Should go to main menu", level.getStageToLoad() == Stage.MAIN_MENU);
    }

}
