package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.Stage;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;

/**
 * Base class for all StageLogic scripts.
 */
public abstract class StageLogic implements IStageLogic, IKeyObserver {

    protected GameStage stage;

    protected Stage stageToLoad;

    protected GameEvent event = null;

    StageLogic() {
        KeyObserve.instance.register(this);
    }

    @Override
    public GameEvent getEvent() {
        return event;
    }

    @Override
    public Stage getStageToLoad() {
        return stageToLoad;
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }

    @Override
    public void setGameStage(final GameStage stage) {
        this.stage = stage;
    }

}