package com.astro.game.script.stage;

import com.astro.game.engine.base.GameEvent;
import com.astro.game.engine.stage.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * * Represents logic of the selected level.
 */
@Slf4j
@Component
public class SettingsLogic extends StageLogic {

    @Override
    public void keyPressEvent(final int keyCode) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            event = GameEvent.SWITCH_STAGE;
            stageToLoad = Stage.MAIN_MENU;
        }
    }

}
