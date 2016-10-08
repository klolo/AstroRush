package com.astro.game.script.stage;

import com.astro.game.engine.base.GameEvent;
import com.astro.game.engine.physics.PhysicsEngine;
import com.astro.game.engine.stage.GameStage;
import com.astro.game.engine.stage.Stage;
import com.astro.game.observe.IKeyObserver;
import com.astro.game.observe.KeyObserve;
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
    protected GameEvent event = null;

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
