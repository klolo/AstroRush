package com.astro.core.engine.stage;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.CameraManager;
import com.astro.core.engine.ParalaxBackground;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.engine.ScreenManager;
import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
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

    @GameProperty("renderer.debug")
    private boolean DEBUG_DRAW = false;

    @GameProperty("renderer.scale")
    private float SCALE = 2.0f;

    private Box2DDebugRenderer renderer = new Box2DDebugRenderer();

    private ParalaxBackground paralaxBackground;

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElements = new ArrayList<>();

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElementsWithLogic = new ArrayList<>();

    @Getter
    private final String stageName;

    /**
     * Creating only by factory. Access package.
     */
    GameStage(final ArrayList<IGameObject> elements, final String name) {
        stageName = name;
        PropertyInjector.instance.inject(this);
        this.mapElements = elements;

        mapElements = ScreenManager.instance.sortObjectsByLayer(mapElements);
        mapElementsWithLogic = ScreenManager.instance.getObjectsWithLogic(mapElements);

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
     * Update and render game.
     */
    @Override
    public void render(float delta) {
        if (paralaxBackground != null) {
            paralaxBackground.show(CameraManager.instance.getCamera(), delta);
        }

        mapElements.forEach(e -> e.show(CameraManager.instance.getCamera(), delta));

        if (DEBUG_DRAW) {
            renderer.render(
                    PhysicsWorld.instance.getWorld(),
                    CameraManager.instance.getCamera().combined.scl(PhysicsWorld.instance.PIXEL_PER_METER)
            );
        }
        PhysicsWorld.instance.getRayHandler().updateAndRender();
    }

    /**
     *
     */
    public void update(float diff) {
        updateCamera();
        mapElementsWithLogic.forEach(e -> e.update(diff));

        if (paralaxBackground != null) {
            paralaxBackground.update(CameraManager.instance.getCamera(), diff);
        }
    }

    private void updateCamera() {
        Vector3 position = CameraManager.instance.getCamera().position;

        position.x = 0f;
        position.y = 0f;

        CameraManager.instance.getCamera().position.set(position);
        CameraManager.instance.getCamera().update();
        PhysicsWorld.instance.getRayHandler().setCombinedMatrix(CameraManager.instance.getCamera());
    }


    @Override
    public void resize(int width, int height) {
        CameraManager.instance.getCamera().setToOrtho(false, width / SCALE, height / SCALE);
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
        PhysicsWorld.instance.getRayHandler().dispose();
    }


    private void destroyPhysicsBody(IGameObject gameObject) {
        if (gameObject instanceof PhysicsObject) {
            log.info("Destroy body: {}", ((PhysicsObject) gameObject).getBodyName());
            PhysicsWorld.instance.getWorld().destroyBody(((PhysicsObject) gameObject).getBody());
        }
    }


    public void unregister() {
        mapElements.forEach(e -> destroyPhysicsBody(e));
    }
}
