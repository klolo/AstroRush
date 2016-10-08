package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.Stage;
import com.badlogic.gdx.Input;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of the garage stage, where user can for example change weapon.
 */
public class GarageLogicTest {

    @Test
    public void shouldReturnToLevelStage() {
        //given
        final GarageLogic garageLogic = new GarageLogic();

        //when
        garageLogic.keyPressEvent(Input.Keys.ESCAPE);

        //then
        Assert.assertTrue("Should garage return SWITCH_STAGE event", garageLogic.getEvent().equals(GameEvent.SWITCH_STAGE));
        Assert.assertTrue("Should garage want load LEVEL1", garageLogic.getStageToLoad().equals(Stage.LEVEL1));
    }

}
