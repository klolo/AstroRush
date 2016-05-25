package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.Stages;

/**
 * Created by kamil on 25.05.16.
 */
public interface IStageLogic {

    void setGameStage(final GameStage stage);

    GameEvent getEvent();

    Stages getStageToLoad();

}
