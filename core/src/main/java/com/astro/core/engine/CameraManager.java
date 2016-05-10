package com.astro.core.engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Marcin Białecki on 2016-05-09.
 * <p>
 * Hold the OrthographicCamera instance. Object that observe the player by implementing {@link IObservedByCamera}.
 * Currently react on player moves.
 */
public enum CameraManager {
    instance;

    @Getter
    private OrthographicCamera camera;

    private static final float PLAYER_X_POSITION = 0.8f;

    @Setter
    private IObservedByCamera observedObject;

    /**
     * Constructor.
     */
    CameraManager() {
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 0, 0);
        camera.position.set(0f, 0f, 0f);
    }

    /**
     * Set new player position from the observedObject or set on center.
     */
    public void update() {
        Vector3 position = camera.position;

        if (observedObject != null) {
            position.x = observedObject.getPositionX() * PhysicsWorld.instance.PIXEL_PER_METER;
            position.y = observedObject.getPositionY() * PhysicsWorld.instance.PIXEL_PER_METER * PLAYER_X_POSITION;
        }
        else {
            position.x = 0.0f;
            position.y = 0.0f;
        }


        camera.position.set(position);
        camera.update();
        PhysicsWorld.instance.getRayHandler().setCombinedMatrix(camera);
    }

}
