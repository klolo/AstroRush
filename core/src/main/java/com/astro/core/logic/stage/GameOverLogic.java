package com.astro.core.logic.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.Stage;
import com.badlogic.gdx.Input;
import org.springframework.stereotype.Component;

@Component
public class GameOverLogic extends StageLogic {

    @Override
    public void keyPressEvent(final int keyCode) {
        switch (keyCode) {
            case Input.Keys.ESCAPE: {
                processEscapeKey();
                break;
            }
        }
    }

    private void processEscapeKey() {
        event = GameEvent.SWITCH_STAGE;
        stageToLoad = Stage.MAIN_MENU;
    }

}
