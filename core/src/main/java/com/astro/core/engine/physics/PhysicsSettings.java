package com.astro.core.engine.physics;

import lombok.Data;

/**
 * Aggregate all settings for PhysicsWorld.
 */
@Data
class PhysicsSettings {

    /**
     * Strength of gravity.
     */
    protected float gravity = 0.0f;

    /**
     * Localization of the ground box.
     */
    protected float groundX = 0.0f;

    /**
     * Localization of the ground box.
     */
    protected float groundY = 0.0f;

    /**
     * Size of the ground box.
     */
    protected float groundWidth = 0.0f;

    /**
     * Size of the ground box.
     */
    protected float groundHeight = 0.0f;

    /**
     * Density of the ground box.
     */
    protected float groundDensity = 0.0f;

    /**
     * Density of the ground box.
     */
    protected int velocityIterations = 0;

    /**
     * Density of the ground box.
     */
    protected int positionIterations = 0;

    /**
     * Density of the ground box.
     */
    protected float timeStep = 0;

    /**
     * Density of the ground box.
     */
    protected int pixelPerMeter = 0;

    /**
     * Debug render for Box2D
     */
    protected boolean debugDraw = false;
}
