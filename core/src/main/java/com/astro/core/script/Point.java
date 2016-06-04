package com.astro.core.script;

import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.Getter;

/**
 * Created by kamil on 11.05.16.
 */
public class Point implements ILogic {

    /**
     * Message which will show above the player after the collision.
     */
    @Getter
    private String playerMsg = "+10";

    private IGameObject gameObject;

    @Override
    public void update(float diff) {

    }

    public void setGameObject(IGameObject gameObject) {
        this.gameObject = gameObject;
        gameObject.getData().setCollisionConsumer(this::collisionEvent);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    public void collisionEvent(IGameObject collidatedObject) {
        if (Player.IDENTIFIER.equals(collidatedObject.getData().getItemIdentifier())) {
            gameObject.getData().setDestroyed(true);
        }
    }

    @Override
    public void additionalRender(OrthographicCamera cam, float delta) {

    }
}
