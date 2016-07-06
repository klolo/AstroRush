package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.Stage;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for all StageLogic scripts.
 */
@Slf4j
public abstract class StageLogic implements IStageLogic, IKeyObserver {

    @Setter
    @Autowired
    protected PhysicsEngine physicsEngine;

    @Setter
    protected GameStage gameStage;

    @Getter
    protected Stage stageToLoad;

    @Getter
    @Setter
    protected GameEvent event = null;

    StageLogic() {
        KeyObserve.instance.register(this);
    }

    public void init() {
        log.info("Creating stage logic");
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
