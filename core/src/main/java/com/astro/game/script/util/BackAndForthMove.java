package com.astro.game.script.util;

import com.astro.game.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.BodyDef;


public class BackAndForthMove<T extends IGameObject> {

    private T gameObject;

    private float minPosX;

    private float maxPosY;

    private final float speed;

    private float currentSpeed;

    public BackAndForthMove(final T gameObject, final float minX, final float maxX, final float speed) {
        this.gameObject = gameObject;
        this.minPosX = minX;
        this.maxPosY = maxX;
        this.speed = Math.abs(speed);
        this.currentSpeed = this.speed;
    }

    public void update(final float diff) {
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

}
