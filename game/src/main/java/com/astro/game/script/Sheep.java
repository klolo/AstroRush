package com.astro.game.script;

import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;

/**
 * Test logic for sheep.
 */
public class Sheep implements ILogic {

    private AnimationObject gameObject;

    float speed = 1f;

    public void setGameObject(IGameObject gameObject) {
        this.gameObject = (AnimationObject) gameObject;
    }

    @Override
    public void update(float diff) {
        float posX = gameObject.getSprite().getX();
        if (posX > 2) {
            speed = -1f;
            gameObject.setFlipX(true);
        }
        else if (posX < -2) {
            speed = 1;
            gameObject.setFlipX(false);
        }
        gameObject.getSprite().setX(posX + diff * speed);
    }

}
