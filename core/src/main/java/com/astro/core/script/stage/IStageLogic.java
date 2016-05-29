package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.Stage;

/**
 * Common interface for all stages logic class, used in GameScreen.
 */
public interface IStageLogic {

    /**
     * Set the instance of the GameStage, where this logic is attached.
     */
    void setGameStage(final GameStage stage);

    /**
     * Get event to invoke in GameLogic class.
     */
    GameEvent getEvent();

    /**
     * If event is change stage, this should return stage to load.
     */
    Stage getStageToLoad();

}
