package com.astro.core.overlapAdapter;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.uwsoft.editor.renderer.data.LightVO;

import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.PointLight;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 27.04.16.
 */
@Slf4j
public class LightsLoader {

    @GameProperty("renderer.scale")
    private float SCALE = 2.0f;

    private ArrayList<Light> lights = new ArrayList<>();

    /**
     * Size of the screen.
     */
    private float width = 0.0f;

    /**
     * Size of the screen.
     */
    private float height = 0.0f;

    @GameProperty("renderer.pixel.per.meter")
    @Getter
    private int PIXEL_PER_METER = 0;

    public LightsLoader() {
        new PropertyInjector(this);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    /**
     *
     * @param light
     */
    public void register(LightVO light) {
        log.info("Create light");
        PointLight pointLight = new PointLight(
                PhysicsWorld.instance.getRayHandler(),
                light.rays*100,
                new Color(
                        light.tint[0],
                        light.tint[1],
                        light.tint[2],
                        light.tint[3]),
                light.distance * PIXEL_PER_METER/2,
                light.x * PIXEL_PER_METER - (light.scaleX / 2) ,
                light.y * PIXEL_PER_METER - (light.scaleY / 2)
        );
        pointLight.setSoftnessLength(light.softnessLength);
        pointLight.setSoft(true);
        lights.add(pointLight);
    }
}
