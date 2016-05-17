package com.astro.core.script.player;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;

/**
 * Hold all costants player settings.
 */
public class PlayerSettings {

    public float MAX_Y_VELOCITY = 15f;

    public float MAX_Y_POSITION = 15f;

    public float playerHeight = 0.0f;

    /**
     * Amount of the pixel per meter.
     */
    @GameProperty("renderer.pixel.per.meter")
    public int PIXEL_PER_METER = 0;

    public PlayerSettings() {
        PropertyInjector.instance.inject(this);
    }

}
