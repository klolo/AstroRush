package com.astro.core.engine.stage;

import com.astro.core.adnotation.processor.DisposeCaller;
import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.base.GameEngine;
import com.astro.core.engine.base.ParallaxBackground;
import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.ObjectData;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.TextureObject;
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

import java.util.ArrayList;

/**
 * Represents game stage/level.
 */
@Slf4j
public class GameStage implements Screen {

    @Setter
    @Value("${renderer.scale}")
    private float scale = 2.0f;

    @Setter
    @Value("${renderer.debug}")
    private boolean debugDraw = false;

    @Setter
    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter = 0;

    @Setter
    private IGameHud hud;

    private ParallaxBackground parallaxBackground;

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElements = new ArrayList<>();

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElementsWithLogic = new ArrayList<>();

    /**
     * Debug rendering physics.
     */
    @Setter
    @Autowired
    private Box2DDebugRenderer renderer;

    @Setter
    @Getter
    private IStageLogic stageLogic;

    @Setter
    @Autowired
    private PhysicsEngine physicsEngine;


    private CameraManager cameraManager;

    /**
     * Creating only by factory. Access package.
     */
    GameStage() {

    }

    void initStage(final ArrayList<IGameObject> elements) {
        this.mapElements = elements;
        mapElements = GameObjectUtil.instance.sortObjectsByLayer(mapElements);

        mapElementsWithLogic = GameObjectUtil.instance.getObjectsWithLogic(mapElements);
        ObjectsRegister.instance.registerPhysicsObject(mapElementsWithLogic);

        cameraManager = GameEngine.getApplicationContext().getBean(CameraManager.class);
    }

    void initBackground() {
        parallaxBackground = new ParallaxBackground();
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
    public void render(float delta) {
        if (parallaxBackground != null) {
            parallaxBackground.show(cameraManager.getCamera(), delta);
        }

        mapElements.forEach(e -> e.show(cameraManager.getCamera(), delta));
        mapElementsWithLogic.stream()
                .filter(element -> ((TextureObject) element).isRenderingInScript())
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

        ArrayList<IGameObject> currentMapElementsWithLogic = new ArrayList<>(mapElementsWithLogic);
        currentMapElementsWithLogic.forEach(e -> processGameObjects(e, diff));

        if (parallaxBackground != null) {
            parallaxBackground.update(cameraManager.getCamera(), diff);
        }
    }

    private void processGameObjects(final IGameObject gameObject, float diff) {
        gameObject.update(diff);
        final ObjectData gameData = gameObject.getData();

        removeDestroyedElements(gameObject, gameData);
        loadChildren(gameData);
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

    private void removeDestroyedElements(final IGameObject gameObject, final ObjectData gameData) {
        if (gameData.isDestroyed()) {
            mapElementsWithLogic.remove(gameObject);
            mapElements.remove(gameObject);
            physicsEngine.destroyBody(gameData.getBody());
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
        log.info("dispose");
        DisposeCaller disposer = new DisposeCaller();
        mapElements.forEach(disposer::callDispose);
        physicsEngine.dispose();
    }

    /**
     * Remove object when stage is changing.
     */
    public void unregister() {
        log.info("unregister stage");
        mapElementsWithLogic.forEach(obj -> obj.getData().getLogic().onPause());
        stageLogic.onPause();
        KeyObserve.instance.unregister(stageLogic);
    }

    /**
     * Remove object when stage is changing.
     */
    public void register() {
        log.info("register stage");
        mapElementsWithLogic.forEach(obj -> obj.getData().getLogic().onResume());
        stageLogic.onResume();
        KeyObserve.instance.register(stageLogic);
    }

    public void unregisterPhysics() {
        mapElements.forEach(this::destroyPhysicsBody);
        mapElementsWithLogic.forEach(this::destroyPhysicsBody);
    }

    /**
     * Remove Box2D objects.
     */
    private void destroyPhysicsBody(final IGameObject gameObject) {
        if (gameObject.isPhysicObject()) {
            log.info("Destroy body: {}", gameObject.getData().getName());
            physicsEngine.destroyBody(gameObject.getData().getBody());
            gameObject.getData().setBody(null);
        }
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