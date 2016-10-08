package com.astro.game.engine.stage;

import com.astro.game.engine.physics.PhysicsEngine;
import com.astro.game.objects.ObjectsRegister;
import com.astro.game.objects.interfaces.IGameObject;
import com.astro.game.overlap_runtime.OverlapSceneReader;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Creating GameStage based on json configuration loaded to StageConfig object.
 */
@Slf4j
@Component
public class StageFactory implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Autowired
    private GameObjectUtil gameObjectUtil;

    @Autowired
    private PhysicsEngine physicsEngine;

    @Autowired
    private OverlapSceneReader overlapSceneReader;

    @Autowired
    private ObjectsRegister objectsRegister;

    public GameStage create(final StageConfig config) {
        final long startTime = System.currentTimeMillis();
        LOGGER.info("Loading stage: {}", config.stageName);

        prepareGameForStage(config);

        final GameStage result = applicationContext.getBean(GameStage.class);
        result.initStage(getMapElements(config));
        result.setConfig(config);

        initResult(result)
                .createBackground(config, result)
                .initPlayer(config, result)
                .initLogic(config, result)
                .initDebugDraw(config, result);

        LOGGER.info("Stage loaded, time: {} ms", (System.currentTimeMillis() - startTime));
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
            LOGGER.info("Create HUD");
            final IGameHud hud = applicationContext.getBean(IGameHud.class);
            hud.init();
            result.setHud(hud);
        }
        return this;
    }

    private StageFactory initDebugDraw(final StageConfig config, final GameStage result) {
        if (!config.hasPlayer) {
            LOGGER.info("disable physics debug draw on this stage");
            result.setDebugDraw(false);
        }
        return this;
    }

    private StageFactory createBackground(final StageConfig config, final GameStage result) {
        if (!"".equals(config.background)) {
            LOGGER.info("Init background");
            result.initBackground();
        }
        return this;
    }

    /**
     * Set ambient color of the light. When you want convert hex value to opengl float
     * use: http://tools.ix.cx/tools/hex_to_opengl_color_1.html
     */
    private void prepareGameForStage(final StageConfig config) {
        LOGGER.info("Loading light");
        physicsEngine.setAmbientLight(config.ambientLightRed, config.ambientLightGreen, config.ambientLightBlue);
    }

    /**
     * Reading from json all entities on the screen.
     */
    private ArrayList<IGameObject> getMapElements(final StageConfig config) {
        LOGGER.info("Reading json");
        final ArrayList<IGameObject> result = (ArrayList<IGameObject>) overlapSceneReader.loadScene(config.sceneFile);

        addToObjectRegister(result);
        return gameObjectUtil.sortObjectsByLayer(result);
    }

    /**
     * Register all object with set ID.
     */
    private void addToObjectRegister(final ArrayList<IGameObject> objects) {
        objectsRegister.registerObjects(
                objects.stream()
                        .filter(this::shouldBeObjectRegister)
                        .collect(Collectors.toList())
        );
    }

    private boolean shouldBeObjectRegister(final IGameObject object) {
        return object != null
                && object.getData().getItemIdentifier() != null
                && !"".equals(object.getData().getItemIdentifier());
    }
}
