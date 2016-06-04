package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.physics.PhysicsWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import lombok.extern.slf4j.Slf4j;

/**
 * * Represents logic of the selected level.
 */
@Slf4j
public class LevelLogic extends StageLogic {

    @Override
    public void keyPressEvent(int keyCode) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            processEsc();
        }
    }

    void processEsc() {
        event = GameEvent.PREV_STAGE;
    }

    @Override
    public void onPause() {
        PhysicsWorld.instance.setFroozePhysicsProcessing(true);
    }

    @Override
    public void onResume() {
        PhysicsWorld.instance.setFroozePhysicsProcessing(false);
    }
}
