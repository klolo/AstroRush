package com.astro.core.overlap_runtime.loaders;

import com.astro.core.engine.stage.GameObjectUtil;
import com.astro.core.objects.LightObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.LightVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class convert LightVO from json to box2d light and register in game.
 */
@Slf4j
@Component
public class LightsLoader extends BaseLoader implements ILoader<LightVO>, ApplicationContextAware {

    @Autowired
    @Setter
    private ApplicationContext applicationContext;

    @Autowired
    private GameObjectUtil gameObjectUtil;

    /**
     * @param light
     */
    public IGameObject register(final LightVO light) {
        gameObjectUtil.addLayer(light.layerName);

        final LightObject lightObject = applicationContext.getBean(LightObject.class);
        lightObject.initLogic(light);
        return lightObject;
    }
}
