package com.astro.core.overlap_runtime.loaders;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.LayerManager;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.uwsoft.editor.renderer.data.LightVO;

import java.util.ArrayList;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class convert LightVO from json to box2d light and register in game.
 */
@Slf4j
public class LightsLoader implements ILoader<LightVO> {

    /**
     * Readed lights.
     */
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
    public IGameObject register(LightVO light) {
        log.info("[register light] name: {}", light.itemName);
        Light resultLight;

        if (light.type == LightVO.LightType.POINT) {
            resultLight = new PointLight(
                    PhysicsWorld.instance.getRayHandler(),
                    light.rays * 10,
                    new Color(
                            light.tint[0],
                            light.tint[1],
                            light.tint[2],
                            .5f),
                    light.distance * PIXEL_PER_METER,
                    light.x * PIXEL_PER_METER - (light.scaleX / 2),
                    light.y * PIXEL_PER_METER - (light.scaleY / 2)
            );
        }
        else{
            resultLight = new ConeLight(
                    PhysicsWorld.instance.getRayHandler(),
                    light.rays * 10,
                    new Color(
                            light.tint[0],
                            light.tint[1],
                            light.tint[2],
                            .5f),
                    light.distance * PIXEL_PER_METER,
                    light.x * PIXEL_PER_METER - (light.scaleX / 2),
                    light.y * PIXEL_PER_METER - (light.scaleY / 2),
                    light.directionDegree,
                    light.coneDegree
            );
        }

        resultLight.setSoftnessLength(light.softnessLength);
        resultLight.setXray(light.isXRay);
        resultLight.setStaticLight(light.isStatic);
        lights.add(resultLight);
        LayerManager.instance.addLayer(light.layerName);
        return  null;
    }
}
