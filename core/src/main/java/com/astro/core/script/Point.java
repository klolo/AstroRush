package com.astro.core.script;

import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;

/**
 * Created by kamil on 11.05.16.
 */
public class Point implements ILogic {

    private IGameObject gameObject;

    @Override
    public void update(float diff) {

    }

    @Override
    public void setGameObject(IGameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public void collision(IGameObject collidatedObject, boolean collisionStart) {
        if ("player".equals(collidatedObject.getItemIdentifier())) {
            gameObject.setIsDestroyed(true);
        }
    }
}
