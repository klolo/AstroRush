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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base class for all StageLogic scripts.
 */
@Slf4j
public abstract class StageLogic implements IStageLogic, IKeyObserver, InitializingBean {

    @Autowired
    protected PhysicsEngine physicsEngine;

    @Setter
    protected GameStage gameStage;

    protected Stage stageToLoad;

    @Autowired
    private KeyObserve keyObserve;

    @Getter
    @Setter
    protected GameEvent event;

    public void afterPropertiesSet() {
        keyObserve.register(this);
    }

    public void init() {
        LOGGER.info("Creating stage logic");
    }

    public Stage getStageToLoad() {
        final Stage result = stageToLoad;
        stageToLoad = null;
        return result;
    }

}
