package com.astro.core.engine.stage;

import lombok.Data;

/**
 * Represent configuration of the screen loaded from json file.
 */
@Data
public class StageConfig {

    public String stageName = "";

    public String sceneFile = "";

    public boolean hasPlayer;

    public boolean followByPlayer;

    public String background = "";

    public boolean hasHUD;

}
