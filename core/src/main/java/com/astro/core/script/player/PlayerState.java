package com.astro.core.script.player;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;
import lombok.Getter;

/**
 * Represent and determine player state.
 */
public enum PlayerState {

    /**
     * Player stands on the ground and does not move.
     */
    STAND,
    /**
     * Player is above the ground and move to left.
     */
    FLY_LEFT,
    /**
     * * Player is above the ground and move to right.
     */
    FLY_RIGHT,
    /**
     * Player is on the ground and move to left.
     */
    RUN_LEFT,
    /**
     * Player is on the ground and move to right.
     */
    RUN_RIGHT;

    @Getter
    @GameProperty("player.min.move")
    private float MINIMAL_PLAYER_MOVE;

    /**
     * -Injecting game property
     */
    PlayerState() {
        PropertyInjector.instance.inject(this);
    }

    /**
     * Determinates player move kind.
     */
    public PlayerState getState(final float lastX, final float lastY, final float currentX, final float currentY, final PlayerState currentState) {
        final float moveX = lastX - currentX;
        final float moveY = lastY - currentY;

        if (Math.abs(moveX) < MINIMAL_PLAYER_MOVE && Math.abs(moveY) < MINIMAL_PLAYER_MOVE) {
            return STAND;
        }
        else if (!isMove(moveY)) {
            if (currentState == FLY_RIGHT || currentState == FLY_LEFT) {
                return currentState;
            }
            return currentX > lastX ? RUN_RIGHT : RUN_LEFT;
        }
        else {
            return currentX > lastX ? FLY_RIGHT : FLY_LEFT;
        }
    }

    /**
     * Checking that player is moving.
     */
    private boolean isMove(final float move) {
        return Math.abs(move) > MINIMAL_PLAYER_MOVE;
    }

    public boolean isRun() {
        return this == PlayerState.RUN_LEFT || this == PlayerState.RUN_RIGHT;
    }

    public boolean isFly() {
        return this == PlayerState.FLY_LEFT || this == PlayerState.FLY_RIGHT;
    }

    public PlayerState getPlayerStateAfterRightKey() {
        if (this == PlayerState.FLY_LEFT) {
            return PlayerState.FLY_RIGHT;
        }
        else if (this == PlayerState.RUN_LEFT) {
            return PlayerState.RUN_RIGHT;
        }
        else {
            return PlayerState.RUN_RIGHT;
        }
    }

    public PlayerState getPlayerStateAfterLeftKey() {
        if (this == PlayerState.FLY_RIGHT) {
            return PlayerState.FLY_LEFT;
        }
        else if (this == PlayerState.RUN_RIGHT) {
            return PlayerState.RUN_LEFT;
        }
        else {
            return PlayerState.RUN_LEFT;
        }
    }

}
