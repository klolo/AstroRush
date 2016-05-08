package com.astro.core.script;

import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 23.04.16.
 */
@Slf4j
public class Player implements ILogic, IKeyObserver {

    private AnimationObject gameObject;

    Body body;

    public Player() {
        KeyObserve.instance.register(this);
    }

    public void setGameObject(IGameObject gameObject) {
        this.gameObject = (AnimationObject) gameObject;
        ((AnimationObject) gameObject).getBody().setFixedRotation(true);
        body =  ((AnimationObject) gameObject).getBody();
    }

    @Override
    public void update(float diff) {
        gameObject.getSprite().setPosition(body.getPosition().x, body.getPosition().y);
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
            body.applyForceToCenter(0, 40, false);
        }

        body.setLinearVelocity(horizontalForce * 5, body.getLinearVelocity().y);
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }
}
