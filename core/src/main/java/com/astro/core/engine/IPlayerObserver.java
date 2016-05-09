package com.astro.core.engine;

/**
 * Created by Marcin Białecki on 2016-05-09.
 */
public interface IPlayerObserver {

    /**
     * Update player postion event.
     *
     * @param playerMove Player move parameters.
     */
    void updatePosition(final PlayerMove playerMove);

}
