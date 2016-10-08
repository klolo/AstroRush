package com.astro.game.script;

import com.astro.game.objects.TextureObject;
import com.astro.game.objects.interfaces.IGameObject;
import com.astro.game.objects.interfaces.ILogic;
import com.astro.game.script.util.BackAndForthMove;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MovingPlatform implements ILogic {

    private BackAndForthMove move;

    @Override
    public void update(float diff) {
        move.update(diff);
    }

    public void setGameObject(IGameObject gameObject) {
        move = new BackAndForthMove<>((TextureObject) gameObject, -5f, 0f, 2f);
    }
}
