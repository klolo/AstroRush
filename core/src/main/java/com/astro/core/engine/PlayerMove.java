package com.astro.core.engine;

import lombok.Getter;

/**
 * Created by Marcin Białecki on 2016-05-09.
 *
 * Object that represent player's move. It is sending with player move event.
 */
public class PlayerMove {

    @Getter
    float positionX;

    @Getter
    float positionY;

    /**
     * Constructor.
     *
     * @param positionX X position value.
     * @param positionY Y postion value.
     */
    public PlayerMove(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

}
