package com.astro.core.logic.stage;

import com.astro.core.engine.base.GameEvent;
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
    public void keyPressEvent(final int keyCode) {
        switch (keyCode) {
            case Input.Keys.ESCAPE: {
                processEscapeKey();
                break;
            }
        }
    }

    private void processEscapeKey() {
        event = GameEvent.RESUME;
    }

}
