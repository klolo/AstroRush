package com.astro.core.overlap_runtime.loaders;

/**
 * Represent a loader from json map.
 */

import com.astro.core.objects.interfaces.IGameObject;

public interface ILoader<T> {

    /**
     * Register method for implementation.
     */
    IGameObject register(T t);
}
