package com.astro.core.logic.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.Stage;
import com.astro.core.observe.IKeyObserver;

/**
 * Common interface for all stages logic class, used in GameScreen.
 */
public interface IStageLogic extends IKeyObserver {

    /**
     * Set the instance of the GameStage, where this logic is attached.
     */
    void setGameStage(final GameStage stage);

    /**
     * Get event to invoke in GameLogic class.
     */
    GameEvent getEvent();

    /**
     * Set event after use.
     */
    void setEvent(GameEvent event);

    /**
     * If event is change stage, this should return stage to load.
     */
    Stage getStageToLoad();

    default void onPause() {

    }

    default void onResume() {

    }

    void init();
}
