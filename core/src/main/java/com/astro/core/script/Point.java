package com.astro.core.script;

import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import lombok.Getter;


public class Point implements ILogic {

    /**
     * Message which will show above the player after the collision.
     */
    @Getter
    String playerMsg = "+10";

    private IGameObject gameObject;

    public void setGameObject(final IGameObject gameObject) {
        this.gameObject = gameObject;
        gameObject.getData().setCollisionConsumer(this::collisionEvent);
    }

    public void collisionEvent(final IGameObject collidatedObject) {
        if (Player.IDENTIFIER.equals(collidatedObject.getData().getItemIdentifier())) {
            gameObject.getData().setDestroyed(true);
        }
    }

}
