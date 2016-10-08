package com.astro.game.script.stage;

import com.astro.game.engine.base.GameEvent;
import com.astro.game.engine.stage.GameStage;
import com.astro.game.engine.stage.Stage;
import com.astro.game.observe.IKeyObserver;

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
