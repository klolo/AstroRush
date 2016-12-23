package com.astro.core.logic;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.badlogic.gdx.graphics.Color;
import com.uwsoft.editor.renderer.data.LightVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class LightLogic implements ILogic {

    @Autowired
    protected PhysicsEngine physicsEngine;

    @Setter
    @Value("${renderer.pixel.per.meter}")
    public short pixelPerMeter;

    @Value("${renderer.light.distance}")
    @Setter
    private int lightDistance;

    @Setter
    private LightVO light;

    @Override
    public void setGameObject(final IGameObject gameObject) {

    }

    @Override
    public void onResume() {
        registerLight();
    }

    public void registerLight() {
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
        LOGGER.info("Created light: {}", resultLight);
    }
}
