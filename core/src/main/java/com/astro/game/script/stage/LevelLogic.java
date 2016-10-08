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
public class LevelLogic extends StageLogic {

    @Override
    public void keyPressEvent(int keyCode) {
        switch (keyCode) {
            case Input.Keys.ESCAPE: {
                processEscapeKey();
                break;
            }
            case Input.Keys.TAB: {
                processTabKey();
                break;
            }
        }
    }

    private void processTabKey() {
        event = GameEvent.SWITCH_STAGE;
        stageToLoad = Stage.GARAGE;
    }

    void processEscapeKey() {
        event = GameEvent.SWITCH_STAGE;
        stageToLoad = Stage.MAIN_MENU;
    }

    @Override
    public void onPause() {
        physicsEngine.setFroozePhysicsProcessing(true);
    }

    @Override
    public void onResume() {
        physicsEngine.setFroozePhysicsProcessing(false);
    }

}