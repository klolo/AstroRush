package com.astro.core.engine.stage;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.DisposeCaller;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.base.ParallaxBackground;
import com.astro.core.engine.physics.PhysicsWorld;
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

import java.util.ArrayList;

/**
 * Represents game stage/level.
 */
@Slf4j
public class GameStage implements Screen {

    @GameProperty("renderer.scale")
    private float SCALE = 2.0f;

    @GameProperty("renderer.debug")
    @Setter
    private boolean DEBUG_DRAW = false;

    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

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
    private Box2DDebugRenderer renderer = new Box2DDebugRenderer();

    @Setter
    @Getter
    private IStageLogic stageLogic;

    /**
     * Creating only by factory. Access package.
     */
    GameStage(final ArrayList<IGameObject> elements) {
        PropertyInjector.instance.inject(this);

        this.mapElements = elements;
        mapElements = GameObjectUtil.instance.sortObjectsByLayer(mapElements);

        mapElementsWithLogic = GameObjectUtil.instance.getObjectsWithLogic(mapElements);
        ObjectsRegister.instance.registerPhysicsObject(mapElementsWithLogic);

        log.info("end loading game");
    }

    void initBackground() {
        parallaxBackground = new ParallaxBackground();
        parallaxBackground.init();
    }

    /**
     * Constructs a new OrthographicCamera, using the given viewport width and height Height is multiplied by aspect ratio.
     */
    public void init() {
        CameraManager.instance.getCamera().setToOrtho(false, 0f, 0f);
        CameraManager.instance.getCamera().position.set(0f, 0f, 0f);
    }

    /**
     * Update and render game. Order is important.
     */
    @Override
    public void render(float delta) {
        if (parallaxBackground != null) {
            parallaxBackground.show(CameraManager.instance.getCamera(), delta);
        }

        mapElements.forEach(e -> e.show(CameraManager.instance.getCamera(), delta));
        mapElementsWithLogic.stream()
                .filter(element -> ((TextureObject) element).isRenderingInScript())
                .forEach(e -> e.getData().getLogic().additionalRender(CameraManager.instance.getCamera(), delta));

        PhysicsWorld.instance.getRayHandler().updateAndRender();

        if (hud != null) {
            hud.show(CameraManager.instance.getCamera(), delta);
        }

        debugDraw();
    }

    private void debugDraw() {
        if (DEBUG_DRAW) {
            renderer.render(
                    PhysicsWorld.instance.getWorld(),
                    CameraManager.instance.getCamera().combined.scl(PIXEL_PER_METER)
            );
        }
    }

    public void update(final float diff) {
        updateCamera();

        ArrayList<IGameObject> currentMapElementsWithLogic = new ArrayList<>(mapElementsWithLogic);
        currentMapElementsWithLogic.forEach(e -> processGameObjects(e, diff));

        if (parallaxBackground != null) {
            parallaxBackground.update(CameraManager.instance.getCamera(), diff);
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
                    .filter(e -> e.isPhysicObject())
                    .forEach(object -> mapElementsWithLogic.add(object));
            gameData.getLogic().getChildren().forEach(object -> mapElements.add(object));
            gameData.setHasChild(false);
        }
    }

    private void removeDestroyedElements(final IGameObject gameObject, final ObjectData gameData) {
        if (gameData.isDestroyed()) {
            mapElementsWithLogic.remove(gameObject);
            mapElements.remove(gameObject);
            PhysicsWorld.instance.getWorld().destroyBody(gameData.getBody());
        }
    }

    private void updateCamera() {
        CameraManager.instance.update();
        PhysicsWorld.instance.getRayHandler().setCombinedMatrix(CameraManager.instance.getCamera());
    }

    @Override
    public void resize(int width, int height) {
        CameraManager.instance.getCamera().setToOrtho(false, width / SCALE, height / SCALE);
        if (parallaxBackground != null) {
            parallaxBackground.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        log.info("dispose");
        DisposeCaller disposer = new DisposeCaller();
        mapElements.forEach(disposer::callDispose);
        PhysicsWorld.instance.getRayHandler().dispose();
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
    private void destroyPhysicsBody(IGameObject gameObject) {
        if (gameObject.isPhysicObject()) {
            log.info("Destroy body: {}", gameObject.getData().getName());
            PhysicsWorld.instance.getWorld().destroyBody(gameObject.getData().getBody());
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