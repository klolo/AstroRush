package com.astro.core.objects;

import com.astro.core.script.LightLogic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.uwsoft.editor.renderer.data.LightVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Contains light data and logic.
 */
@Slf4j
public class LightObject extends GameObject implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    public void initLogic(final LightVO light) {
        final LightLogic lightLogic = applicationContext.getBean(LightLogic.class);
        lightLogic.setLight(light);
        lightLogic.setGameObject(this);
        lightLogic.registerLight();
        data.setLogic(lightLogic);
    }

    @Override
    protected void render(final OrthographicCamera cam, final float delta) {

    }

    @Override
    public void show(final OrthographicCamera cam, final float delta) {

    }

}
