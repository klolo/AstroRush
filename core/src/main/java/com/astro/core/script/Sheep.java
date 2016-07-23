package com.astro.core.script;

import com.astro.core.engine.physics.CollisionProcessResult;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.script.util.BackAndForthMove;
import lombok.extern.slf4j.Slf4j;

/**
 * Test logic for sheep.
 */
@Slf4j
public class Sheep implements ILogic {

    private AnimationObject gameObject;

    private BackAndForthMove move;

    public void setGameObject(final IGameObject gameObject) {
        this.gameObject = (AnimationObject) gameObject;
        this.gameObject.getData().setCollisionCallbackFunction(this::collisionEvent);

        move = new BackAndForthMove<>((AnimationObject) gameObject, -2f, 2f, 1f);
    }

    CollisionProcessResult collisionEvent(final IGameObject gameObject) {
        log.debug("hit sheep");
        return new CollisionProcessResult();
    }

    @Override
    public void update(final float diff) {
        move.update(diff);
    }

}
