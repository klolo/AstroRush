package com.astro.core.objects.interfaces;

/**
 * Represents a logic of the game object.
 */
public interface ILogic {

    void update(float diff);

    void setGameObject(IGameObject gameObject);

}
