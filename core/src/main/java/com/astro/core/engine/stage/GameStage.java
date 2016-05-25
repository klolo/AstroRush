package com.astro.core.engine.stage;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.DisposeCaller;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.base.ParalaxBackground;
import com.astro.core.engine.physics.PhysicsWorld;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
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
    private boolean DEBUG_DRAW = false;

    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    @Setter
    private IGameHud hud;

    private ParalaxBackground paralaxBackground;

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElements = new ArrayList<>();

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElementsWithLogic = new ArrayList<>();

    private Box2DDebugRenderer renderer = new Box2DDebugRenderer();

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

    public void initBackground() {
        paralaxBackground = new ParalaxBackground();
        paralaxBackground.init();
    }

    /**
     * Constructs a new OrthographicCamera, using the given viewport width and height Height is multiplied by aspect ratio.
     */
    public void init() {
        CameraManager.instance.getCamera().setToOrtho(false, 0f, 0f);
        CameraManager.instance.getCamera().position.set(0f, 0f, 0f);
    }


    @Override
    public void show() {

    }

    /**
     * Update and render game. Order is important.
     */
    @Override
    public void render(float delta) {
        if (paralaxBackground != null) {
            paralaxBackground.show(CameraManager.instance.getCamera(), delta);
        }

        mapElements.forEach(e -> e.show(CameraManager.instance.getCamera(), delta));
        mapElementsWithLogic.forEach(e -> e.getData().getLogic().additionalRender(CameraManager.instance.getCamera(), delta));

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

    /**
     *
     */
    public void update(float diff) {
        updateCamera();

        ArrayList<IGameObject> currentMapElementsWithLogic = (ArrayList<IGameObject>) mapElementsWithLogic.clone();
        currentMapElementsWithLogic.forEach(e -> processGameObjects(e, diff));

        if (paralaxBackground != null) {
            paralaxBackground.update(CameraManager.instance.getCamera(), diff);
        }
    }

    private void processGameObjects(IGameObject gameObject, float diff) {
        gameObject.update(diff);

        if (gameObject.getData().isDestroyed()) {
            mapElementsWithLogic.remove(gameObject);
            mapElements.remove(gameObject);
            PhysicsWorld.instance.getWorld().destroyBody(gameObject.getData().getBody());
        }
    }

    private void updateCamera() {
        CameraManager.instance.update();
        PhysicsWorld.instance.getRayHandler().setCombinedMatrix(CameraManager.instance.getCamera());
    }


    @Override
    public void resize(int width, int height) {
        CameraManager.instance.getCamera().setToOrtho(false, width / SCALE, height / SCALE);
        if (paralaxBackground != null) {
            paralaxBackground.resize(width, height);
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
    public void dispose() {
        log.info("dispose");
        DisposeCaller disposer = new DisposeCaller();
        mapElements.forEach(e -> disposer.callDispose(e));
        PhysicsWorld.instance.getRayHandler().dispose();
    }

    /**
     * Remove object when stage is changing.
     */
    public void unregister() {
        log.info("unregister stage");
        mapElements.forEach(e -> destroyPhysicsBody(e));
        CameraManager.instance.setObservedObject(null);
        mapElements = null;
        mapElementsWithLogic = null;
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

}