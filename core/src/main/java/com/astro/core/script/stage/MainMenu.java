package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.Stages;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents logic of the MainMenu.
 */
@Slf4j
public class MainMenu extends StageLogic {

    @Override
    public void keyPressEvent(int keyCode) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            event = GameEvent.GAME_EXIT;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            event = GameEvent.SWITCH_STAGE;
            stageToLoad = Stages.LEVEL1;
        }
    }

}
