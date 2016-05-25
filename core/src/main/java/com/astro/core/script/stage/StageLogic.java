package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.Stages;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;

/**
 * Created by kamil on 25.05.16.
 */
public abstract class StageLogic implements IStageLogic, IKeyObserver {

    protected GameStage stage;

    protected Stages stageToLoad;

    protected GameEvent event = null;

    StageLogic() {
        KeyObserve.instance.register(this);
    }

    @Override
    public GameEvent getEvent() {
        return event;
    }

    @Override
    public Stages getStageToLoad() {
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
