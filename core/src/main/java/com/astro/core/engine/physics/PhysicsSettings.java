package com.astro.core.engine.physics;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Aggregate all settings for PhysicsWorld.
 */
@Data
@ToString
@Component
class PhysicsSettings {

    /**
     * Strength of gravity.
     */
    @Value("${world.gravity}")
    protected float gravity;

    /**
     * Localization of the ground box.
     */
    @Value("${world.ground.x}")
    protected float groundX;

    /**
     * Localization of the ground box.
     */
    @Value("${world.ground.y}")
    protected float groundY;

    /**
     * Size of the ground box.
     */
    @Value("${world.ground.width}")
    protected float groundWidth;

    /**
     * Size of the ground box.
     */
    @Value("${world.ground.height}")
    protected float groundHeight;

    /**
     * Density of the ground box.
     */
    @Value("${world.ground.density}")
    protected float groundDensity;

    /**
     * Density of the ground box.
     */
    @Value("${world.iteration.velocity}")
    protected int velocityIterations;

    /**
     * Density of the ground box.
     */
    @Value("${world.iteration.position}")
    protected int positionIterations;

    /**
     * Density of the ground box.
     */
    @Value("${world.time.step}")
    protected float timeStep;

    /**
     * Density of the ground box.
     */
    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter;

    /**
     * Debug render for Box2D
     */
    @Value("${renderer.debug}")
    protected boolean debugDraw;
}
