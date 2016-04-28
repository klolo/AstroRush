package com.astro.core.engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.IGameObject;
import com.astro.core.objects.IPlayer;
import com.astro.core.overlapAdapter.OverlapSceneReader;
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

    @Setter
    @Getter
    private ArrayList<IGameObject> mapElements = new ArrayList<>();

    private float width;

    private float height;
    /**
     *
     */
    public GameScreen() {
        new PropertyInjector(this);
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initCamera();

        OverlapSceneReader sceneReader = new OverlapSceneReader("scenes/MainScene.dt")
                .loadScene();

        mapElements = (ArrayList<IGameObject>) sceneReader.readAndRegisterComponents();
        sceneReader.registerLights();

        log.info("end");
    }

    /**
     * Constructs a new OrthographicCamera, using the given viewport width and height
     * Height is multiplied by aspect ratio.
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
     * @param delta
     */
    @Override
    public void render(float delta) {
        mapElements.forEach(e -> e.render(camera));
        player.render(camera);
        if (DEBUG_DRAW) {
            renderer.render(
                    PhysicsWorld.instance.getWorld(),
                    camera.combined.scl(PhysicsWorld.instance.getPIXEL_PER_METER())
            );
        }

        PhysicsWorld.instance.getRayHandler().updateAndRender();
    }


    public void update() {
        updateCemera();
    }


    private void updateCemera() {
        Vector3 position = camera.position;
        position.x = player.getPositionX() * PhysicsWorld.instance.getPIXEL_PER_METER();
        position.y = player.getPositionY() * PhysicsWorld.instance.getPIXEL_PER_METER()/2;
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
