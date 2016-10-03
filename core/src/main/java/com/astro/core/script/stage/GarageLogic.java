package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.Stage;
import com.badlogic.gdx.Input;
import lombok.extern.slf4j.Slf4j;

/**
 * * Represents logic of the selected level.
 */
@Slf4j
public class GarageLogic extends StageLogic {

    @Override
    public void keyPressEvent(int keyCode) {
        switch (keyCode) {
            case Input.Keys.ESCAPE: {
                processEscapeKey();
                break;
            }
        }
    }

    private void processEscapeKey() {
        event = GameEvent.SWITCH_STAGE;
        stageToLoad = Stage.LEVEL1;
    }

}
