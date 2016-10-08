package com.astro.core.overlap_runtime.converters;

import com.astro.core.objects.interfaces.IGameObject;

/**
 * Base interface for all loaders.
 */
interface IConverter<T> {

    IGameObject convert(T input,IGameObject output);

}
