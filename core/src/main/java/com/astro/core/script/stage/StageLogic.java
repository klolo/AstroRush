package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.Stage;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for all StageLogic scripts.
 */
@Slf4j
public abstract class StageLogic implements IStageLogic, IKeyObserver {

    protected GameStage stage;

    protected Stage stageToLoad;

    public void init() {
        log.info("Creating stage logic");
    }

    @Getter
    @Setter
    protected GameEvent event = null;

    StageLogic() {
        KeyObserve.instance.register(this);
    }

    @Override
    public Stage getStageToLoad() {
        return stageToLoad;
    }


    @Override
    public void setGameStage(final GameStage stage) {
        this.stage = stage;
    }

    @Override
    public void onPause() {
        log.info("onPause");
    }

    @Override
    public void onResume() {
        log.info("onResume");
    }

}
