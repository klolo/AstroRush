package com.astro.core.overlapAdapter;

/**
 * Represent a loader from json map.
 */

import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.resources.ResourceManager;

public interface ILoader<T> {

    /**
     * It will load effects by name.
     */
    ResourceManager resourceManager = new ResourceManager();

    /**
     * Register method for implementation.
     */
    IGameObject register(T t);
}
