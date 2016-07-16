package com.astro.core.engine.base;

import com.astro.core.engine.interfaces.IObservedByCamera;
import com.astro.core.engine.physics.PhysicsEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Marcin Białecki on 2016-05-09.
 * <p>
 * Hold the OrthographicCamera instance. Object that observe the player by implementing {@link IObservedByCamera}.
 * Currently react on player moves.
 */
public class CameraManager {

    @Getter
    private OrthographicCamera camera;

    @Setter
    private IObservedByCamera observedObject;

    /**
     * Density of the ground box.
     */
    @Setter
    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter;

    @Getter
    @Autowired
    private PhysicsEngine physicsEngine;

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
        final Vector3 position = camera.position;

        if (observedObject != null) {
            position.x = observedObject.getPositionX() * pixelPerMeter;
            position.y = observedObject.getPositionY() * pixelPerMeter;
        }
        else {
            position.x = 0.0f;
            position.y = 0.0f;
        }

        camera.position.set(position);
        camera.update();
        physicsEngine.setCamera(camera);
    }

}
