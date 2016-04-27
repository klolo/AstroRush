package com.astro.core.engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.IGameObject;
import com.astro.core.objects.IPlayer;
import com.astro.core.overlapAdapter.OverlapSceneReader;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.PointLight;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 23.04.16.
 */
@Slf4j
public class GameScreen implements Screen {

    @GameProperty("renderer.debug")
    private boolean DEBUG_DRAW;

    @GameProperty("renderer.scale")
    private float SCALE = 2.0f;

    private OrthographicCamera camera;

    @Setter
    private IPlayer player;

    private Box2DDebugRenderer renderer = new Box2DDebugRenderer();

    private ArrayList<Light> lights = new ArrayList<Light>(1);

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElements = new ArrayList<>();

    private PointLight pointLight;

    /**
     *
     */
    public GameScreen() {
        new PropertyInjector(this);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        // Constructs a new OrthographicCamera, using the given viewport width and height
        // Height is multiplied by aspect ratio.
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width / SCALE, height / SCALE);

        createLight();

        OverlapSceneReader sceneReader = new OverlapSceneReader("scenes/MainScene.dt")
                .loadScene();

        mapElements = (ArrayList<IGameObject>) sceneReader.registerComponents();


        log.info("end");
    }

    private void createLight() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        PhysicsWorld.instance.getRayHandler().setCombinedMatrix(camera);
        pointLight = new PointLight(
                PhysicsWorld.instance.getRayHandler(),
                5000,
                new Color(.2f, .2f, .8f, 1f),
                2000,
                (width / SCALE) - 100,
                (height / SCALE) - 100
        );
        pointLight.setSoftnessLength(30);
        pointLight.setSoft(true);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
//        sceneLoader.getEngine().update(delta);
        PhysicsWorld.instance.getRayHandler().render();

        mapElements.forEach(e -> e.render(camera));
        player.render(camera);

        if (DEBUG_DRAW) {
            renderer.render(
                    PhysicsWorld.instance.getWorld(),
                    camera.combined.scl(PhysicsWorld.instance.getPIXEL_PER_METER())
            );
        }
    }


    public void update() {
        PhysicsWorld.instance.getRayHandler().update();
        updateCemera();
    }


    private void updateCemera() {
        Vector3 position = camera.position;
        position.x = player.getPositionX() * PhysicsWorld.instance.getPIXEL_PER_METER();
        position.y = player.getPositionY() * PhysicsWorld.instance.getPIXEL_PER_METER();
        camera.position.set(position);
        camera.update();
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
        pointLight.dispose();
    }
}
