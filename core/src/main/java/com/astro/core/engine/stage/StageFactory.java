package com.astro.core.engine.stage;

import com.astro.core.engine.base.GameEngine;
import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.OverlapSceneReader;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Creating GameStage based on json configuration loaded to StageConfig object.
 */
@Slf4j
public class StageFactory implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Setter
    @Autowired
    private PhysicsEngine physicsEngine;

    @Setter
    @Autowired
    private OverlapSceneReader overlapSceneReader;

    public GameStage create(final StageConfig config) {
        final long startTime = System.currentTimeMillis();
        log.info("Loading stage: {}", config.stageName);

        prepareGameForStage(config);

        final GameStage result = GameEngine.getApplicationContext().getBean(GameStage.class);
        result.initStage(getMapElements(config));

        initResult(result)
                .createBackground(config, result)
                .initPlayer(config, result)
                .initLogic(config, result)
                .initDebugDraw(config, result);

        log.info("Stage loaded, time: {} ms", (System.currentTimeMillis() - startTime));
        return result;
    }

    private StageFactory initResult(final GameStage result) {
        result.init();
        return this;
    }

    /**
     * Create Stage logic class and put it to the stage.
     */
    private StageFactory initLogic(final StageConfig config, final GameStage result) {
        config.stageLogic.init();
        result.setStageLogic(config.stageLogic);
        return this;
    }

    private StageFactory initPlayer(final StageConfig config, final GameStage result) {
        if (config.hasPlayer) {
            log.info("Create HUD");
            IGameHud hud = applicationContext.getBean(IGameHud.class);
            hud.init();
            result.setHud(hud);
        }
        return this;
    }

    private StageFactory initDebugDraw(final StageConfig config, final GameStage result) {
        if (!config.hasPlayer) {
            log.info("disable physics debug draw on this stage");
            result.setDebugDraw(false);
        }
        return this;
    }

    private StageFactory createBackground(final StageConfig config, final GameStage result) {
        if (!"".equals(config.background)) {
            log.info("Init background");
            result.initBackground();
        }
        return this;
    }

    /**
     * Set ambient color of the light. When you want convert hex value to opengl float
     * use: http://tools.ix.cx/tools/hex_to_opengl_color_1.html
     */
    private void prepareGameForStage(final StageConfig config) {
        log.info("Loading light");
        physicsEngine.setAmbientLight(config.ambientLightRed, config.ambientLightGreen, config.ambientLightBlue);
    }

    /**
     * Reading from json all entities on the screen.
     */
    private ArrayList<IGameObject> getMapElements(final StageConfig config) {
        log.info("Reading json");
        final ArrayList<IGameObject> result = (ArrayList<IGameObject>) overlapSceneReader.loadScene(config.sceneFile);

        addToObjectRegister(result);
        return GameObjectUtil.instance.sortObjectsByLayer(result);
    }

    /**
     * Register all object with set ID.
     */
    private void addToObjectRegister(final ArrayList<IGameObject> objects) {
        ObjectsRegister.instance.registerObject(
                objects.stream()
                        .filter(e -> shouldBeObjectRegister(e))
                        .collect(Collectors.toList())
        );
    }

    private boolean shouldBeObjectRegister(final IGameObject object) {
        return object != null
                && object.getData().getItemIdentifier() != null
                && !"".equals(object.getData().getItemIdentifier());
    }
}
