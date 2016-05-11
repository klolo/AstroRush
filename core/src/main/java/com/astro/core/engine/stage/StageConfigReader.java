package com.astro.core.engine.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Created by kamil on 05.05.16.
 */
public class StageConfigReader {

    /**
     * Data from stages.json file.
     */
    private StageConfig[] stages;

    /**
     * Path to configuration file.
     */
    private final String STAGES_CONFIG_FILE = "stages.json";

    /**
     * Reading configuration file.
     */
    private void read() {
        FileHandle file = Gdx.files.internal(STAGES_CONFIG_FILE);
        stages = new Json().fromJson(StageConfigReader.class, file.readString()).stages;
    }

    /**
     * Return configuraion if exists or reading it.
     * @return
     */
    public StageConfig[] getConfigs() {
        if (stages == null) {
            read();
        }
        return stages;
    }

}