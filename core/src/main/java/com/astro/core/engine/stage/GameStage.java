package com.astro.core.engine.stage;

import com.astro.core.adnotation.processor.DisposeCaller;
import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.base.ParallaxBackground;
import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.ObjectData;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.observe.KeyObserve;
import com.astro.core.script.stage.IStageLogic;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Represents game stage/level.
 */
@Slf4j
@Component
@Scope("prototype")
@Getter
@Setter
public class GameStage implements Screen, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Value("${renderer.scale}")
    private float scale = 2.0f;

    @Autowired
    private ObjectsRegister objectsRegister;

    @Autowired
    private GameObjectUtil gameObjectUtil;

    @Value("${renderer.debug}")
    private boolean debugDraw;

    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter;

    private IGameHud hud;

    private ParallaxBackground parallaxBackground;

    private ArrayList<IGameObject> mapElements = new ArrayList<>();

    private ArrayList<IGameObject> mapElementsWithLogic = new ArrayList<>();

    /**
     * Debug rendering physics.
     */
    @Autowired
    private Box2DDebugRenderer renderer;

    @Autowired
    private KeyObserve keyObserve;

    private IStageLogic stageLogic;

    @Autowired
    private PhysicsEngine physicsEngine;

    private StageConfig config;

    @Autowired
    private CameraManager cameraManager;

    void initStage(final ArrayList<IGameObject> elements) {
        this.mapElements = elements;
        mapElements = gameObjectUtil.sortObjectsByLayer(mapElements);

        mapElementsWithLogic = gameObjectUtil.getObjectsWithLogic(mapElements);
        objectsRegister.registerPhysicsObjects(mapElementsWithLogic);
    }

    void initBackground() {
        parallaxBackground = applicationContext.getBean(ParallaxBackground.class);
        parallaxBackground.init();
    }

    /**
     * Constructs a new OrthographicCamera, using the given viewport width and height Height is multiplied by aspect ratio.
     */
    public void init() {
        cameraManager.getCamera().setToOrtho(false, 0f, 0f);
        cameraManager.getCamera().position.set(0f, 0f, 0f);
    }

    /**
     * Update and render game. Order is important.
     */
    @Override
    public void render(final float delta) {
        if (parallaxBackground != null) {
            parallaxBackground.show(cameraManager.getCamera(), delta);
        }

        mapElements.forEach(e -> e.show(cameraManager.getCamera(), delta));
        mapElementsWithLogic.stream()
                .filter(IGameObject::isRenderingInScript)
                .forEach(e -> e.getData().getLogic().additionalRender(cameraManager.getCamera(), delta));

        physicsEngine.updateAndRenderLight();

        if (hud != null) {
            hud.show(cameraManager.getCamera(), delta);
        }

        debugDraw();
    }

    private void debugDraw() {
        if (debugDraw) {
            renderer.render(
                    physicsEngine.getWorld(),
                    cameraManager.getCamera().combined.scl(pixelPerMeter)
            );
        }
    }

    public void update(final float diff) {
        updateCamera();

        final ArrayList<IGameObject> currentMapElementsWithLogic = new ArrayList<>(mapElementsWithLogic);
        currentMapElementsWithLogic.forEach(e -> processGameObjects(e, diff));

        if (parallaxBackground != null) {
            parallaxBackground.update(cameraManager.getCamera(), diff);
        }
    }

    private void processGameObjects(final IGameObject gameObject, final float diff) {
        gameObject.update(diff);
        removeDestroyedElements(gameObject);
        loadChildren(gameObject.getData());
    }

    private void loadChildren(final ObjectData gameData) {
        if (gameData.isHasChild()) {
            gameData.getLogic()
                    .getChildren()
                    .stream()
                    .filter(GameObject::isPhysicObject)
                    .forEach(object -> mapElementsWithLogic.add(object));
            gameData.getLogic().getChildren().forEach(object -> mapElements.add(object));
            gameData.setHasChild(false);
        }
    }

    private void removeDestroyedElements(final IGameObject gameObject) {
        if (gameObject.getData().isDestroyed()) {
            LOGGER.info("Remove object: {}", gameObject);
            mapElementsWithLogic.remove(gameObject);
            mapElements.remove(gameObject);
            physicsEngine.destroyBody(gameObject.getData().getBody());
        }
    }

    private void updateCamera() {
        cameraManager.update();
        physicsEngine.setCombinedMatrix(cameraManager.getCamera());
    }

    @Override
    public void resize(final int width, final int height) {
        cameraManager.getCamera().setToOrtho(false, width / scale, height / scale);
        if (parallaxBackground != null) {
            parallaxBackground.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        LOGGER.info("dispose");
        final DisposeCaller disposer = new DisposeCaller();
        mapElements.forEach(disposer::callDispose);
        physicsEngine.dispose();
    }

    public void destroy() {
        unregister();
        //       objectsRegister.clear();
        physicsEngine.destroyAllBodies();
    }

    /**
     * Remove object when stage is changing.
     */
    public void unregister() {
        LOGGER.info("unregister stage");
        mapElementsWithLogic.parallelStream().forEach(obj -> obj.getData().getLogic().onPause());
        stageLogic.onPause();
        keyObserve.unregister(stageLogic);
    }

    /**
     * Remove object when stage is changing.
     */
    public void register() {
        LOGGER.info("register stage");
        //   initStage(mapElements);
        physicsEngine.initLight(config.ambientLightRed, config.ambientLightGreen, config.ambientLightBlue);
        mapElementsWithLogic.parallelStream().forEach(obj -> obj.getData().getLogic().onResume());
        stageLogic.onResume();
        keyObserve.register(stageLogic);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void show() {
    }
}