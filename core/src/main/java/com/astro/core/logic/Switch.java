package com.astro.core.logic;

import com.astro.core.engine.physics.CollisionProcessResult;
import com.astro.core.logic.player.IInteractWithPlayer;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;


public class Switch implements ILogic, IInteractWithPlayer {

    private TextureObject gameObject;

    @Override
    public void update(final float diff) {

    }

    @Override
    public void setGameObject(final IGameObject gameObject) {
        this.gameObject = (TextureObject) gameObject;
        gameObject.getData().setCollisionCallbackFunction(this::collisionEvent);
    }

    public CollisionProcessResult collisionEvent(final IGameObject collidatedObject) {
        return new CollisionProcessResult();
    }

    @Override
    public void interact() {
        if (gameObject.getData().isFlipX()) {
            gameObject.getData().setFlipX(false);
        }
        else {
            gameObject.getData().setFlipX(true);
        }

    }
}
