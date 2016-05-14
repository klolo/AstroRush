package com.astro.core.script;

import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.script.Player;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by kamil on 11.05.16.
 */
public class Point implements ILogic {

    public static final String IDENTIFIER = "point";

    private IGameObject gameObject;

    @Override
    public void update(float diff) {

    }

    @Override
    public void setGameObject(IGameObject gameObject) {
        this.gameObject = gameObject;
        gameObject.getData().setCollisionConsumer(this::collisionEvent);
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
