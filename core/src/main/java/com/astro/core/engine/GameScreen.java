package com.astro.core.engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.IPlayer;
import com.astro.core.overlap_runtime.OverlapSceneReader;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 23.04.16.
 */
@Slf4j
public class GameScreen implements Screen {

    @GameProperty("renderer.debug")
    private boolean DEBUG_DRAW = false;

    @GameProperty("renderer.scale")
    private float SCALE = 2.0f;

    private OrthographicCamera camera;

    @Setter
    private IPlayer player;

    private Box2DDebugRenderer renderer = new Box2DDebugRenderer();

    private Background background;

    private ParalaxBackground paralaxBackground;

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElements = new ArrayList<>();

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElementsWithLogic = new ArrayList<>();

    private float width;

    private float height;

    /**
     * TODO: show splash screen.
     */
    public GameScreen(String sceneName) {
        new PropertyInjector(this);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initCamera();

        OverlapSceneReader sceneReader = new OverlapSceneReader(sceneName)
                .loadScene();

        mapElements = (ArrayList<IGameObject>) sceneReader.getComponents();

        mapElements = ScreenManager.instance.sortObjectsByLayer(mapElements);
        mapElementsWithLogic =  ScreenManager.instance.getObjectsWithLogic(mapElements);

        background = new Background();
        paralaxBackground = new ParalaxBackground();
        log.info("end loading game");
    }

    /**
     * Constructs a new OrthographicCamera, using the given viewport width and height Height is multiplied by aspect ratio.
     */
    private void initCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width / SCALE, height / SCALE);
    }


    @Override
    public void show() {

    }

    /**
     * Update and render game.
     */
    @Override
    public void render(float delta) {
     //   background.show(camera,delta);
        paralaxBackground.show(camera,delta);

        mapElements.forEach(e -> e.show(camera, delta));
        player.show(camera, delta);

        if (DEBUG_DRAW) {
            renderer.render(
                    PhysicsWorld.instance.getWorld(),
                    camera.combined.scl(PhysicsWorld.instance.getPIXEL_PER_METER())
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

        background.update(camera,diff);
        paralaxBackground.update(camera,diff);
    }

    private static final float PLAYER_X_POSITION = 0.3f;

    private void updateCemera() {
        Vector3 position = camera.position;
        position.x = player.getPositionX() * PhysicsWorld.instance.getPIXEL_PER_METER();
        position.y = player.getPositionY() * PhysicsWorld.instance.getPIXEL_PER_METER() * PLAYER_X_POSITION;
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
}
