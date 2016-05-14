package com.astro.core.script;

import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.interfaces.IObservedByCamera;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO:
 * - dodac texture kiedy gracz stoi w miejscu i ja wyswietlac zamiast animacji
 * - dodac obsluge kolizji z innymi obiektami
 * - usunac obiekt player z engine i dodac sledzenie kamery na tym obiekcie
 */
@Slf4j
public class Player implements ILogic, IKeyObserver, IObservedByCamera {

    /**
     * Animation of the player.
     */
    private AnimationObject gameObject;

    /**
     * Physics body.
     */
    private Body body;

    //TODO: move to properties or other class
    private float MAX_Y_VELOCITY = 15f;

    private float MAX_Y_POSITION = 15f;

    public Player() {
        KeyObserve.instance.register(this);
        CameraManager.instance.setObservedObject(this);
    }

    public void setGameObject(IGameObject gameObject) {
        this.gameObject = (AnimationObject) gameObject;
        gameObject.getData().setCollisionConsumer(this::collisionEvent);
        body = gameObject.getData().getBody();
        body.setFixedRotation(true);
    }

    @Override
    public void update(float diff) {
        if (body.getPosition().y > MAX_Y_POSITION) {
            body.setTransform(body.getPosition().x, MAX_Y_POSITION, 0);
        }
        gameObject.getData().getSprite().setPosition(body.getPosition().x, body.getPosition().y);
    }

    @Override
    public void keyPressEvent(int keyCode) {
        int horizontalForce = 0;
        if (Input.Keys.LEFT == keyCode) {
            horizontalForce -= 1;
            gameObject.setFlipX(true);
        }
        else if (Input.Keys.RIGHT == keyCode) {
            horizontalForce += 1;
            gameObject.setFlipX(false);
        }
        else if (Input.Keys.UP == keyCode) {
            jump();
        }

        body.setLinearVelocity(horizontalForce * 5, body.getLinearVelocity().y);
    }

    private void jump() {
        if (body.getLinearVelocity().y < MAX_Y_VELOCITY) {
            body.applyForceToCenter(0, MAX_Y_VELOCITY, false);
        }
    }

    public void collisionEvent(IGameObject collidatedObject) {
        log.info("player collision");
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }

    @Override
    public float getPositionX() {
        return body.getPosition().x;
    }

    @Override
    public float getPositionY() {
        return body.getPosition().y;
    }
}
