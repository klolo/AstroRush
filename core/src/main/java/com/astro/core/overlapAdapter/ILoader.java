package com.astro.core.overlapAdapter;

/**
 * Represent a loader from json map.
 */

import com.astro.core.objects.interfaces.IGameObject;

public interface ILoader<T> {

    IGameObject register(T t);
}
