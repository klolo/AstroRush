package com.astro.core.script.util;

import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by kamil on 18.05.16.
 */
public class BackAndForthMove<T extends IGameObject> {

    private T gameObject;

    private float minPosX = 0.0f;

    private float maxPosY = 0.0f;

    private final float speed;

    private float currentSpeed;

    public BackAndForthMove(final T gameObject, float minX, float maxX, float speed) {
        this.gameObject = gameObject;
        this.minPosX = minX;
        this.maxPosY = maxX;
        this.speed = Math.abs(speed);
        this.currentSpeed = this.speed;
    }

    public void update(float diff) {
        float posX = gameObject.getData().getSprite().getX();
        posX += diff * currentSpeed;
        gameObject.getData().getSprite().setX(posX);

        if (gameObject.getData().getBody().getType() == BodyDef.BodyType.KinematicBody) {
            gameObject.getData().getBody().setLinearVelocity(currentSpeed, 0);
        }
        else {
            gameObject.getData().getBody().setTransform(posX, gameObject.getData().getSprite().getY(), 0);
        }

        if (posX > maxPosY) {
            currentSpeed = -1;
            gameObject.getData().setFlipX(true);
        }
        else if (posX < minPosX) {
            currentSpeed = 1;
            gameObject.getData().setFlipX(false);
        }
    }

    private void flipSpeed() {
        if (currentSpeed < 0) {
            currentSpeed = speed;
        }
        else {
            currentSpeed = -1 * speed;
        }
    }

}
