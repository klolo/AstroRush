package com.astro.core.script;

import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.Getter;


public class Point implements ILogic {

    /**
     * Message which will show above the player after the collision.
     */
    @Getter
    String playerMsg = "+10";

    private IGameObject gameObject;

    @Override
    public void update(final float diff) {

    }

    public void setGameObject(final IGameObject gameObject) {
        this.gameObject = gameObject;
        gameObject.getData().setCollisionConsumer(this::collisionEvent);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    public void collisionEvent(final IGameObject collidatedObject) {
        if (Player.IDENTIFIER.equals(collidatedObject.getData().getItemIdentifier())) {
            gameObject.getData().setDestroyed(true);
        }
    }

    @Override
    public void additionalRender(final OrthographicCamera cam, final float delta) {

    }
}
