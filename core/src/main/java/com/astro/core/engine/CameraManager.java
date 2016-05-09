package com.astro.core.engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;

/**
 * Created by Marcin Białecki on 2016-05-09.
 *
 * Hold the OrthographicCamera instance. Object that observe the player by implementing {@link IPlayerObserver}.
 * Currently react on player moves.
 */
public enum CameraManager implements IPlayerObserver {
    instance;

    @GameProperty("renderer.scale")
    private float SCALE = 2.0f;

    @Getter
    private OrthographicCamera camera;

    private float width = 0.0f;

    private float height = 0.0f;

    private static final float PLAYER_X_POSITION = 0.3f;

    /**
     * Constructor.
     */
    CameraManager() {
        PropertyInjector.instance.inject(this);
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, width / SCALE, height / SCALE);
        camera.position.set(0f, 0f, 0f);
    }

    @Override
    public void updatePosition(final PlayerMove playerMove) {
        Vector3 position = camera.position;

        position.x = playerMove.getPositionX() * PhysicsWorld.instance.PIXEL_PER_METER;
        position.y = playerMove.getPositionY() * PhysicsWorld.instance.PIXEL_PER_METER * PLAYER_X_POSITION;

        camera.position.set(position);
        camera.update();
        PhysicsWorld.instance.getRayHandler().setCombinedMatrix(camera);
    }

}
