package com.astro.core.engine.stage;

import com.astro.core.engine.ScreenManager;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.OverlapSceneReader;
import com.astro.core.storage.PropertyInjector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * Creating GameStage based on json configuration loaded to StageConfig object.
 */
@Slf4j
public enum StageFactory {
    instance;

    public GameStage create(StageConfig config) {
        log.info("Loading stage: {}", config.stageName);

        GameStage result = new GameStage(getMapElements(config), config.stageName);

        PropertyInjector.instance.inject(result);
        result.init();

        if (!"".equals(config.background)) {
            result.initBackground();
        }

        if (config.hasPlayer) {
            result.setHud(new StageHUD());
        }

        return result;
    }

    /**
     * Reading from json all entities on the screen.
     */
    private ArrayList<IGameObject> getMapElements(StageConfig config) {
        ArrayList<IGameObject> result;
        OverlapSceneReader sceneReader = new OverlapSceneReader(config.sceneFile).loadScene();
        result = (ArrayList<IGameObject>) sceneReader.getComponents();
        return ScreenManager.instance.sortObjectsByLayer(result);
    }

}
