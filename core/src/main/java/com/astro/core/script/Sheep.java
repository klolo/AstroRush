package com.astro.core.script;

import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.extern.slf4j.Slf4j;

/**
 * Test logic for sheep.
 */
@Slf4j
public class Sheep implements ILogic {

    private AnimationObject gameObject;

    float speed = 1f;

    public void setRunAnimation(IGameObject runAnimation) {
        this.gameObject = (AnimationObject) runAnimation;
    }

    @Override
    public void update(float diff) {
        float posX = gameObject.getData().getSprite().getX();
        if (posX > 2) {
            speed = -1f;
            gameObject.setFlipX(true);
        }
        else if (posX < -2) {
            speed = 1;
            gameObject.setFlipX(false);
        }
        gameObject.getData().getSprite().setX(posX + diff * speed);
    }

    @Override
    public void additionalRender(OrthographicCamera cam, float delta) {

    }
}
