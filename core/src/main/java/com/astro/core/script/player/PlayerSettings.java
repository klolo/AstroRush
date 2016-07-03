package com.astro.core.script.player;

import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Hold all player settings.
 */
@Component
public class PlayerSettings {

    @Setter
    public float maxYVelocity;

    @Setter
    public float maxYPosition;

    @Setter
    public float playerHeight;

    @Setter
    public float inactiveMsgTime;

    @Setter
    public float interactWithObjectTime;

    @Setter
    public int pixelPerMeter;

    @Setter
    public String inactivePLayerMessage;

}
