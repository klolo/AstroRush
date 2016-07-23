package com.astro.core.engine.physics;

import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.Contact;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Method for processing calback in collidating objects.
 */
@Slf4j
class CollisionProcessor {

    private final Predicate<IGameObject> shouldBeProcessedObject =
            o -> o != null && o.getData().getCollisionCallbackFunction() != null;

    private final Predicate<IGameObject> shouldReceiveEvent =
            o -> o != null && o.getData() != null;

    @Setter
    private IGameObject firstObject;

    @Setter
    private IGameObject secondObject;

    @Setter
    private Contact contact;

    CollisionProcessor(final IGameObject firstObject, final IGameObject secondObject, final Contact contact) {
        this.firstObject = firstObject;
        this.secondObject = secondObject;
        this.contact = contact;
    }

    /**
     * It will invoke callback method in both collidated objects.
     */
    void process() {
        if (shouldBeProcessedObject.test(firstObject) && shouldReceiveEvent.test(secondObject)) {
            final Function<IGameObject, CollisionProcessResult> function = firstObject.getData().getCollisionCallbackFunction();
            final CollisionProcessResult collisionProcessResult = function.apply(secondObject);

            if (collisionProcessResult.isIgnoreCollision()) {
                contact.setEnabled(false);
            }
        }
    }

}
