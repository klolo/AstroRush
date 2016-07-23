package com.astro.core.engine.physics;

import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Detect collision and send event to object logic class.
 */
@Slf4j
public class CollisionListener implements ContactListener {

    /**
     * Called before collision. Method send event to both collidated objets.
     */
    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        final IGameObject objectA = ObjectsRegister.instance.getObjectByBody(fixtureA.getBody());
        final IGameObject objectB = ObjectsRegister.instance.getObjectByBody(fixtureB.getBody());

        new CollisionProcessor(objectA, objectB, contact).process();
        new CollisionProcessor(objectB, objectA, contact).process();
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
