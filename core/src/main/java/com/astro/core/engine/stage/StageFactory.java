package com.astro.core.engine.stage;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.engine.physics.PhysicsWorld;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.OverlapSceneReader;
import com.astro.core.script.stage.IStageLogic;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Creating GameStage based on json configuration loaded to StageConfig object.
 */
@Slf4j
public enum StageFactory {
    instance;

    @GameProperty("game.logic.script.package")
    private String STAGE_LOGIC_PACKAGE = "";

    @Getter
    private String currentStageName;

    public GameStage create(final StageConfig config) {
        log.info("Loading stage: {}", config.stageName);
        PropertyInjector.instance.inject(this);

        this.currentStageName = config.stageName;

        prepareGameForStage(config);
        GameStage result = new GameStage(getMapElements(config));

        initResult(result)
                .createBackground(config, result)
                .initPlayer(config, result)
                .initLogic(config, result);

        log.info("Stage loaded");
        return result;
    }

    private StageFactory initResult(final GameStage result) {
        PropertyInjector.instance.inject(result);
        result.init();
        return this;
    }

    /**
     * Create Stage logic class and put it to the stage.
     */
    private StageFactory initLogic(final StageConfig config, final GameStage result) {
        try {
            Class clazz = Class.forName(STAGE_LOGIC_PACKAGE + "." + config.stageLogic);
            IStageLogic logic = (IStageLogic) clazz.newInstance();
            result.setStageLogic(logic);
        }
        catch (final ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.error("Error while creating logic class", e);
        }

        return this;
    }

    private StageFactory initPlayer(final StageConfig config, final GameStage result) {
        if (config.hasPlayer) {
            log.info("Create HUD");
            result.setHud(new StageHUD());
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
        PhysicsWorld.instance.setAmbientLight(config.ambientLightRed, config.ambientLightGreen, config.ambientLightBlue);
    }

    /**
     * Reading from json all entities on the screen.
     */
    private ArrayList<IGameObject> getMapElements(final StageConfig config) {
        log.info("Reading json");
        ArrayList<IGameObject> result;
        OverlapSceneReader sceneReader = new OverlapSceneReader(config.sceneFile).loadScene();
        result = (ArrayList<IGameObject>) sceneReader.getComponents();

        addToObjectRegister(result);
        return GameObjectUtil.instance.sortObjectsByLayer(result);
    }

    /**
     * Register all object with set ID.
     */
    private void addToObjectRegister(final ArrayList<IGameObject> objects) {
        ObjectsRegister.instance.registerObject(
                objects.stream()
                        .filter(e -> e.getData().getItemIdentifier() != null && !"".equals(e.getData().getItemIdentifier()))
                        .collect(Collectors.toList())
        );
    }
}
