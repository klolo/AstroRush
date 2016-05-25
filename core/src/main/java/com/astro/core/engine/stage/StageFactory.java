package com.astro.core.engine.stage;

import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.engine.physics.PhysicsWorld;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.OverlapSceneReader;
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

    @Getter
    private String currentStageName;

    public GameStage create(StageConfig config) {
        log.info("Loading stage: {}", config.stageName);
        this.currentStageName = config.stageName;

        prepareGameForStage(config);

        GameStage result = new GameStage(getMapElements(config));

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
     * Set ambient color of the light. When you want convert hex value to opengl float
     * use: http://tools.ix.cx/tools/hex_to_opengl_color_1.html
     */
    private void prepareGameForStage(final StageConfig config) {
        PhysicsWorld.instance.setAmbientLight(config.ambientLightRed, config.ambientLightGreen, config.ambientLightBlue);
    }

    /**
     * Reading from json all entities on the screen.
     */
    private ArrayList<IGameObject> getMapElements(StageConfig config) {
        ArrayList<IGameObject> result;
        OverlapSceneReader sceneReader = new OverlapSceneReader(config.sceneFile).loadScene();
        result = (ArrayList<IGameObject>) sceneReader.getComponents();

        addToObjectRegister(result);
        return GameObjectUtil.instance.sortObjectsByLayer(result);
    }

    /**
     * Register all object with set ID.
     */
    private void addToObjectRegister(ArrayList<IGameObject> objects) {
        ObjectsRegister.instance.clearRegister();
        ObjectsRegister.instance.registerObject(
                objects.stream()
                        .filter(e -> e.getData().getItemIdentifier() != null && !"".equals(e.getData().getItemIdentifier()))
                        .collect(Collectors.toList())
        );
    }
}
