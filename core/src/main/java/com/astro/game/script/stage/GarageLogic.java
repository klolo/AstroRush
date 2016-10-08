package com.astro.game.script.stage;

import com.astro.game.engine.base.GameEvent;
import com.astro.game.engine.stage.Stage;
import com.badlogic.gdx.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * * Represents logic of the selected level.
 */
@Slf4j
@Component
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
