package com.astro.core.script;

import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.extern.slf4j.Slf4j;

/**
 * Test logic for sheep.
 */
@Slf4j
public class Sheep implements ILogic {

    public static final String IDENTIFIER = "sheep";

    private AnimationObject gameObject;

    float speed = 1f;

    /**
     * Physics body.
     */
    private Body body;

    public void setGameObject(IGameObject runAnimation) {
        this.gameObject = (AnimationObject) runAnimation;
        this.gameObject.getData().setCollisionConsumer(this::collisionEvent);
        body = runAnimation.getData().getBody();
    }

    private void collisionEvent(IGameObject gameObject) {
    }

    @Override
    public void update(float diff) {
        float posX = gameObject.getData().getSprite().getX();
        if (posX > 2) {
            speed = -1f;
            gameObject.setFlipX(true);
        }
        else if (posX < -2) {
            speed = 1;
            gameObject.setFlipX(false);
        }

        gameObject.getData().getSprite().setX(posX + diff * speed);
        body.setTransform(posX, gameObject.getData().getSprite().getY(), 0);

    }

    @Override
    public void additionalRender(OrthographicCamera cam, float delta) {

    }
}
