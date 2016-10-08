package com.astro.game.engine.physics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Aggregate all settings for PhysicsWorld.
 */
@Getter
@Setter
@ToString
@Component
class PhysicsSettings {

    /**
     * Strength of gravity.
     */
    @Value("${world.gravity}")
    protected float gravity = 0.0f;

    /**
     * Localization of the ground box.
     */
    @Value("${world.ground.x}")
    protected float groundX = 0.0f;

    /**
     * Localization of the ground box.
     */
    @Value("${world.ground.y}")
    protected float groundY = 0.0f;

    /**
     * Size of the ground box.
     */
    @Value("${world.ground.width}")
    protected float groundWidth = 0.0f;

    /**
     * Size of the ground box.
     */
    @Value("${world.ground.height}")
    protected float groundHeight = 0.0f;

    /**
     * Density of the ground box.
     */
    @Value("${world.ground.density}")
    protected float groundDensity = 0.0f;

    /**
     * Density of the ground box.
     */
    @Value("${world.iteration.velocity}")
    protected int velocityIterations = 0;

    /**
     * Density of the ground box.
     */
    @Value("${world.iteration.position}")
    protected int positionIterations = 0;

    /**
     * Density of the ground box.
     */
    @Value("${world.time.step}")
    protected float timeStep = 0;

    /**
     * Density of the ground box.
     */
    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter = 0;

    /**
     * Debug render for Box2D
     */
    @Value("${renderer.debug}")
    protected boolean debugDraw = false;
}
