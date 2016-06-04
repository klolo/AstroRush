package com.astro.core.script;

import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.script.util.BackAndForthMove;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 14.05.16.
 */
@Slf4j
public class MovingPlatform implements ILogic {

    private IGameObject gameObject;

    private Body body;

    private BackAndForthMove move;

    @Override
    public void update(float diff) {
        move.update(diff);
    }

    public void setGameObject(IGameObject gameObject) {
        this.gameObject = gameObject;
        body = gameObject.getData().getBody();
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
