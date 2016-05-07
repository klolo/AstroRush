package com.astro.core.engine.stage;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.ParalaxBackground;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.engine.ScreenManager;
import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.IPlayer;
import com.astro.core.overlap_runtime.OverlapSceneReader;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    private OrthographicCamera camera;

    private IPlayer player;

    private Box2DDebugRenderer renderer = new Box2DDebugRenderer();

    private ParalaxBackground paralaxBackground;

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElements = new ArrayList<>();

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElementsWithLogic = new ArrayList<>();

    private float width = 0.0f;

    private float height = 0.0f;

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

    public void setPlayer(IPlayer player) {
        this.player = player;
    }

    /**
     * Constructs a new OrthographicCamera, using the given viewport width and height Height is multiplied by aspect ratio.
     */
    public void init() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width / SCALE, height / SCALE);
        camera.position.set(0f, 0f, 0f);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
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
            paralaxBackground.show(camera, delta);
        }

        mapElements.forEach(e -> e.show(camera, delta));

        if (player != null) {
            player.show(camera, delta);
        }

        if (DEBUG_DRAW) {
            renderer.render(
                    PhysicsWorld.instance.getWorld(),
                    camera.combined.scl(PhysicsWorld.instance.PIXEL_PER_METER)
            );
        }
        PhysicsWorld.instance.getRayHandler().updateAndRender();
    }

    /**
     *
     */
    public void update(float diff) {
        updateCemera();
        mapElementsWithLogic.forEach(e -> e.update(diff));

        if (paralaxBackground != null) {
            paralaxBackground.update(camera, diff);
        }
    }

    private static final float PLAYER_X_POSITION = 0.3f;

    private void updateCemera() {
        Vector3 position = camera.position;

        if (player != null) {
            position.x = player.getPositionX() * PhysicsWorld.instance.PIXEL_PER_METER;
            position.y = player.getPositionY() * PhysicsWorld.instance.PIXEL_PER_METER * PLAYER_X_POSITION;
        }
        else {
            position.x = 0f;
            position.y = 0f;
        }

        camera.position.set(position);
        camera.update();
        PhysicsWorld.instance.getRayHandler().setCombinedMatrix(camera);
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / SCALE, height / SCALE);
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
        player.dispose();
    }


    private void destroyPhysicsBody(IGameObject gameObject) {
        if (gameObject instanceof PhysicsObject) {
            log.info("Destroy body: {}", ((PhysicsObject) gameObject).getBodyName());
            PhysicsWorld.instance.getWorld().destroyBody(((PhysicsObject) gameObject).getBody());
        }
    }


    public void unregister() {
        mapElements.forEach(e -> destroyPhysicsBody(e));
        if (player != null) {
            PhysicsWorld.instance.getWorld().destroyBody(player.getBody());
        }
    }
}
