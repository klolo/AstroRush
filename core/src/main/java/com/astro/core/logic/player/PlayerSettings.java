package com.astro.core.logic.player;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Hold all player settings.
 */
@Component
@Scope("prototype")
public class PlayerSettings {

    @Setter
    @Value("${player.configuration.maxYVelocity}")
    public float maxYVelocity;

    @Setter
    @Value("${player.configuration.maxYPosition}")
    public float maxYPosition;

    @Setter
    @Value("${player.configuration.playerHeight}")
    public float playerHeight;

    @Setter
    @Value("${player.configuration.inactiveMsgTime}")
    public float inactiveMsgTime;

    @Setter
    @Value("${player.configuration.interactWithObjectTime}")
    public float interactWithObjectTime;

    @Setter
    @Value("${renderer.pixel.per.meter}")
    public short pixelPerMeter;

    @Setter
    @Value("${player.configuration.inactivePLayerMessage}")
    public String inactivePLayerMessage;

}
