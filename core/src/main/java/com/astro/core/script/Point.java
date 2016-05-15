package com.astro.core.script;

import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.Getter;

/**
 * Created by kamil on 11.05.16.
 */
public class Point implements ILogic {

    public static final String IDENTIFIER = "point";

    /**
     * Message which will show above the player after the collision.
     */
    @Getter
    private String playerMsg = "+10";

    private IGameObject gameObject;

    @Override
    public void update(float diff) {

    }

    public void setRunAnimation(IGameObject runAnimation) {
        this.gameObject = runAnimation;
        runAnimation.getData().setCollisionConsumer(this::collisionEvent);
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
