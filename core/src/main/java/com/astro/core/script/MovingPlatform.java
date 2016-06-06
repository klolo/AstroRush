package com.astro.core.script;

import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.script.util.BackAndForthMove;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void additionalRender(OrthographicCamera cam, float delta) {

    }
}
