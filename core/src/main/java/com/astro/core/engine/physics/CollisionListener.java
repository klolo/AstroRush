package com.astro.core.engine.physics;

import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 11.05.16.
 */
@Slf4j
public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        IGameObject objectA = ObjectsRegister.instance.getObjectByBody(fixtureA.getBody());
        IGameObject objectB = ObjectsRegister.instance.getObjectByBody(fixtureB.getBody());

        if (objectA != null && objectA.hasLogic()) {
            log.debug("Send collision event {}", objectA.getName());
            objectA.getLogic().collision(objectB, true);
        }

        if (objectB != null && objectB.hasLogic()) {
            log.debug("Send collision event {}", objectB.getName());
            objectB.getLogic().collision(objectA, true);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
