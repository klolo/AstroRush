package com.astro.core.engine.physics;

import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Detect collision and send event to object logic class.
 */
@Slf4j
public class CollisionListener implements ContactListener {

    @Autowired
    private ObjectsRegister objectsRegister;

    /**
     * Called before collision. Method send event to both collidated objets.
     */
    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        final IGameObject objectA = objectsRegister.getObjectByBody(fixtureA.getBody());
        final IGameObject objectB = objectsRegister.getObjectByBody(fixtureB.getBody());

        new CollisionProcessor(objectA, objectB, contact).process();
        new CollisionProcessor(objectB, objectA, contact).process();
    }

    @Override
    public void beginContact(final Contact contact) {

    }

    @Override
    public void endContact(final Contact contact) {

    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {

    }
}
