package com.astro.game.script;

import com.astro.game.engine.physics.CollisionProcessResult;
import com.astro.game.objects.TextureObject;
import com.astro.game.objects.interfaces.IGameObject;
import com.astro.game.objects.interfaces.ILogic;
import com.astro.game.script.player.IInteractWithPlayer;


public class Switch implements ILogic, IInteractWithPlayer {

    private TextureObject gameObject;

    @Override
    public void update(final float diff) {

    }

    @Override
    public void setGameObject(IGameObject gameObject) {
        this.gameObject = (TextureObject) gameObject;
        gameObject.getData().setCollisionCallbackFunction(this::collisionEvent);
    }

    public CollisionProcessResult collisionEvent(IGameObject collidatedObject) {
        return new CollisionProcessResult();
    }

    @Override
    public void interact() {
        if(gameObject.getData().isFlipX()) {
            gameObject.getData().setFlipX(false);
        }
        else {
            gameObject.getData().setFlipX(true);
        }

    }
}
