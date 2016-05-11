package com.astro.core.objects.interfaces;

/**
 * Represents a logic of the game object.
 */
public interface ILogic {

    void update(float diff);

    void setGameObject(IGameObject gameObject);

    /**
     * Trigger on collision, but only for physics object, other will be ignore.
     */
    void collision(IGameObject collidatedObject, boolean collisionStart);
}
