package com.astro.game.overlap_runtime.converters;

import com.astro.game.objects.interfaces.IGameObject;

/**
 * Base interface for all loaders.
 */
interface IConverter<T> {

    IGameObject convert(T input,IGameObject output);

}
