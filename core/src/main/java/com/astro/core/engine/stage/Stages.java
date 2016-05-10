package com.astro.core.engine.stage;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 10.05.16.
 */
@Slf4j
public enum Stages {
    /**
     * TODO
     */
    MAIN_MENU,

    /**
     * TODO
     */
    LEVEL1;

    /**
     * TODO: do osobnego pliku
     */
    public Stages mapJsonNameToEnumVal(String stageName) {
        switch (stageName) {
            case "MainMenu": {
                return Stages.MAIN_MENU;
            }
            case "Level1": {
                return Stages.LEVEL1;
            }
            default: {
                log.error("Stage not found");
                return Stages.MAIN_MENU;
            }
        }
    }
}
