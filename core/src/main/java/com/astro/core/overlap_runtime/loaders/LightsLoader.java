package com.astro.core.overlap_runtime.loaders;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.engine.stage.GameObjectUtil;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.graphics.Color;
import com.uwsoft.editor.renderer.data.LightVO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * This class convert LightVO from json to box2d light and register in game.
 */
@Slf4j
public class LightsLoader extends BaseLoader implements ILoader<LightVO> {

    /**
     * Readed lights.
     */
    private ArrayList<Light> lights = new ArrayList<>();

    @GameProperty("renderer.pixel.per.meter")
    @Getter
    @Setter
    private int pixelPerMeter = 0;

    @GameProperty("renderer.light.distance")
    @Setter
    private int lightDistance = 0;

    public LightsLoader(final PhysicsEngine physicsEngine) {
        super(physicsEngine);
        PropertyInjector.instance.inject(this);
    }

    /**
     * @param light
     */
    public IGameObject register(LightVO light) {
        Light resultLight;

        if (light.type == LightVO.LightType.POINT) {
            resultLight = new PointLight(
                    physicsEngine.getRayHandler(),
                    light.rays,
                    new Color(light.tint[0], light.tint[1], light.tint[2], .1f),
                    light.distance * pixelPerMeter * lightDistance,
                    light.x * pixelPerMeter,
                    light.y * pixelPerMeter
            );
        }
        else {
            resultLight = new ConeLight(
                    physicsEngine.getRayHandler(),
                    light.rays,
                    new Color(light.tint[0], light.tint[1], light.tint[2], light.tint[3]),
                    light.distance * pixelPerMeter * lightDistance,
                    light.x * pixelPerMeter,
                    light.y * pixelPerMeter,
                    light.directionDegree,
                    light.coneDegree
            );
        }

        resultLight.setSoft(true);
        resultLight.setSoftnessLength(light.softnessLength);
        resultLight.setXray(light.isXRay);
        resultLight.setStaticLight(light.isStatic);
        lights.add(resultLight);
        GameObjectUtil.instance.addLayer(light.layerName);
        return null;
    }
}
