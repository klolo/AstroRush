package com.astro.game.overlap_runtime.loaders;

/**
 * Represent a loader from json map.
 */

import com.astro.game.objects.interfaces.IGameObject;

public interface ILoader<T> {

    /**
     * Register method for implementation.
     */
    IGameObject register(final T t);
}
