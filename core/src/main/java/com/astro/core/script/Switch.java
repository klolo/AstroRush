package com.astro.core.script;

import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.script.player.IInteractWithPlayer;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by kamil on 16.05.16.
 */
public class Switch implements ILogic, IInteractWithPlayer {

    private TextureObject gameObject;

    @Override
    public void update(float diff) {

    }

    @Override
    public void additionalRender(OrthographicCamera cam, float delta) {

    }

    @Override
    public void setGameObject(IGameObject gameObject) {
        this.gameObject = (TextureObject) gameObject;
        gameObject.getData().setCollisionConsumer(this::collisionEvent);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }


    public void collisionEvent(IGameObject collidatedObject) {

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
