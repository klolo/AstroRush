package com.astro.core.engine.stage;

import com.astro.core.logic.stage.IStageLogic;
import lombok.Data;

/**
 * Represent configuration of the screen loaded from json file.
 */
@Data
public class StageConfig {

    public String sceneFile = "";

    public String stageName = "";

    public boolean hasPlayer;

    public boolean followByPlayer;

    public String background = "";

    public boolean hasHUD;

    public float ambientLightRed;

    public float ambientLightGreen;

    public float ambientLightBlue;

    public IStageLogic stageLogic;
}
