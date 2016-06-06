package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
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
        Assert.assertTrue("By default escape should back to menu", level.getEvent() == GameEvent.PREV_STAGE);
    }

}
