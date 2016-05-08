package com.astro.core.engine.stage;

import com.astro.core.engine.Player;
import com.astro.core.engine.ScreenManager;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.OverlapSceneReader;
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

        OverlapSceneReader sceneReader = new OverlapSceneReader(config.sceneFile)
                .loadScene();

        ArrayList<IGameObject> mapElements = (ArrayList<IGameObject>) sceneReader.getComponents();
        GameStage result = new GameStage(ScreenManager.instance.sortObjectsByLayer(mapElements), config.stageName);

        result.init();

        if (!"".equals(config.background)) {
            result.initBackground();
        }

        if (config.hasPlayer) {
            Player player = new Player();
            result.setPlayer(player);
        }

        return result;
    }

}
