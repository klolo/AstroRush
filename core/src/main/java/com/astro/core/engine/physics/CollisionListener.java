package com.astro.core.engine.physics;

import com.astro.core.objects.ObjectData;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Detect collision and send event to object logic class.
 */
@Slf4j
enum CollisionListener implements ContactListener {
    instance;

    /**
     * Called od collision. Method send event to both collidated objets.
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        IGameObject objectA = ObjectsRegister.instance.getObjectByBody(fixtureA.getBody());
        IGameObject objectB = ObjectsRegister.instance.getObjectByBody(fixtureB.getBody());

        if (objectA == null || objectB == null) {
            return;
        }

        sendEvent(objectA, objectB);
        sendEvent(objectB, objectA);
    }

    /**
     * Call collision event to receiver object. It will send second collision object.
     */
    private void sendEvent(final IGameObject receiver, final IGameObject secondObject) {
        log.debug("Send collision event: from {} to {}",
                secondObject.getData().getName(), receiver.getData().getName());

        Optional.of(receiver)
                .map(IGameObject::getData)
                .map(ObjectData::getCollisionConsumer)
                .ifPresent(consumer -> consumer.accept(secondObject));
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
