package com.astro.core.engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.physics.PhysicsWorld;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.OverlapSceneReader;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

    /**
     * Density of the ground box.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    private Box2DDebugRenderer renderer = new Box2DDebugRenderer();

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
        PropertyInjector.instance.inject(this);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        initCamera();

        OverlapSceneReader sceneReader = new OverlapSceneReader(sceneName)
                .loadScene();

        mapElements = (ArrayList<IGameObject>) sceneReader.getComponents();

        mapElements = ScreenManager.instance.sortObjectsByLayer(mapElements);
        mapElementsWithLogic = ScreenManager.instance.getObjectsWithLogic(mapElements);

        paralaxBackground = new ParalaxBackground();
        paralaxBackground.init();
        log.info("end loading game");
    }

    /**
     * Constructs a new OrthographicCamera, using the given viewport width and height Height is multiplied by aspect ratio.
     */
    private void initCamera() {
        CameraManager.instance.getCamera().setToOrtho(false, width / SCALE, height / SCALE);
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
        paralaxBackground.show(CameraManager.instance.getCamera(), delta);

        mapElements.forEach(e -> e.show(CameraManager.instance.getCamera(), delta));

        if (DEBUG_DRAW) {
            renderer.render(
                    PhysicsWorld.instance.getWorld(),
                    CameraManager.instance.getCamera().combined.scl(PIXEL_PER_METER)
            );
        }
        PhysicsWorld.instance.getRayHandler().updateAndRender();
    }

    /**
     *
     */
    public void update(float diff) {
        mapElementsWithLogic.forEach(e -> e.update(diff));
        paralaxBackground.update(CameraManager.instance.getCamera(), diff);
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
}
