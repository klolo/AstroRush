package com.astro.core.logic;

import com.astro.core.logic.common.BackAndForthMove;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MovingPlatform implements ILogic {

    private BackAndForthMove move;

    @Override
    public void update(final float diff) {
        move.update(diff);
    }

    public void setGameObject(final IGameObject gameObject) {
        move = new BackAndForthMove<>((TextureObject) gameObject, -5f, 0f, 2f);
    }
}
