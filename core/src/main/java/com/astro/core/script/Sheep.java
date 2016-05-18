package com.astro.core.script;

import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.script.util.BackAndForthMove;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.extern.slf4j.Slf4j;

/**
 * Test logic for sheep.
 */
@Slf4j
public class Sheep implements ILogic {

    private AnimationObject gameObject;

    private BackAndForthMove move;

    public void setGameObject(IGameObject gameObject) {
        this.gameObject = (AnimationObject) gameObject;
        this.gameObject.getData().setCollisionConsumer(this::collisionEvent);

        move = new BackAndForthMove<>((AnimationObject) gameObject, -2f, 2f, 1f);
    }

    private void collisionEvent(IGameObject gameObject) {
        log.debug("hit sheep");
    }

    @Override
    public void update(float diff) {
        move.update(diff);
    }

    @Override
    public void additionalRender(OrthographicCamera cam, float delta) {

    }
}
