package com.astro.game.script.stage;

import com.astro.game.engine.base.GameEvent;
import com.astro.game.engine.stage.Stage;
import com.badlogic.gdx.Input;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of main level stage.
 */
public class LevelLogicTest {

    @Test
    public void shouldSwitchToPreviousStage() {
        //given
        final LevelLogic level = new LevelLogic();

        //when
        level.processEscapeKey();

        //then
        Assert.assertTrue("By default escape should back to menu", level.getEvent() == GameEvent.SWITCH_STAGE);
        Assert.assertTrue("Should go to main menu", level.getStageToLoad() == Stage.MAIN_MENU);
    }

    @Test
    public void shouldSwitchStageToGarage() {
        //given
        final LevelLogic level = new LevelLogic();

        //when
        level.keyPressEvent(Input.Keys.TAB);

        //then
        Assert.assertTrue("Should level return SWITCH_STAGE event", level.getEvent().equals(GameEvent.SWITCH_STAGE));
        Assert.assertTrue("Should level want load GARAGE", level.getStageToLoad().equals(Stage.GARAGE));
    }

}
