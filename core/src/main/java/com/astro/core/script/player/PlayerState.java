package com.astro.core.script.player;

/**
 * Created by kamil on 15.05.16.
 */
public enum PlayerState {

    /**
     * Player stands on the ground and does not move.
     */
    STAND,
    FLY_LEFT,
    FLY_RIGHT,
    RUN_LEFT,
    RUN_RIGHT;

    //TODO: move to properties
    private float MINIMAL_PLAYER_MOVE = 0.007f;

    /**
     * Determinates player move kind.
     */
    public PlayerState getState(float lastX, float lastY, float currentX, float currentY) {
        float moveX = lastX - currentX;
        float moveY = lastY - currentY;

        if (Math.abs(moveX) < MINIMAL_PLAYER_MOVE && Math.abs(moveY) < MINIMAL_PLAYER_MOVE) {
            return STAND;
        }
        else if (Math.abs(moveY) < MINIMAL_PLAYER_MOVE) {
            return currentX > lastX ? RUN_RIGHT : RUN_LEFT;
        }
        else {
            return currentX > lastX ? FLY_RIGHT : FLY_LEFT;
        }
    }

    public boolean isRun() {
        return this == PlayerState.RUN_LEFT || this == PlayerState.RUN_RIGHT;
    }

    public boolean isFly() {
        return this == PlayerState.FLY_LEFT || this == PlayerState.FLY_RIGHT;
    }

}
