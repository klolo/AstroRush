package com.astro.core.engine.base;

import com.astro.core.engine.interfaces.IGameLogic;
import com.astro.core.engine.stage.*;
import com.badlogic.gdx.Gdx;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Represents implementation of the Astro Rush 2.0 game.
 */
@Slf4j
public class GameLogic implements IGameLogic {

    /**
     * Stage which is now loaded.
     */
    private Stage currentStage = Stage.MAIN_MENU; // from json file stages file

    /**
     * Loaded from file: stages.json data about stage configuration.
     */
    private HashMap<Stage, StageConfig> screenConfigs = new HashMap<>();

    /**
     * Current game stage loaded from json.
     */
    private GameStage currentScreen;

    /**
     * TODO: doc
     */
    private StageConfigReader configReader = new StageConfigReader();

    /**
     * TODO: doc
     */
    public GameStage getGameScreen() {
        return currentScreen;
    }

    public void init() {
        log.info("AstroGame init");

        Arrays.asList(configReader.getConfigs())
                .stream()
                .collect(Collectors.toList())
                .forEach(s -> screenConfigs.put(Stage.valueOf(s.stageName), s));

        loadStage();
    }

    private void loadStage() {
        if (currentScreen != null) {
            currentScreen.unregister();
            currentScreen.hide();
            currentScreen = null;
        }

        currentScreen = StageFactory.instance.create(screenConfigs.get(currentStage));
    }

    public void render(float deltaTime) {
        currentScreen.render(deltaTime);
    }

    public void update(float time) {
        currentScreen.update(time);
        processEvent();
    }

    private void processEvent() {
        GameEvent event = currentScreen.getStageLogic().getEvent();
        if (event == null) {
            return;
        }

        switch (event) {
            case GAME_EXIT: {
                Gdx.app.exit();
                break;
            }
            case SWITCH_STAGE: {
                currentStage = currentScreen.getStageLogic().getStageToLoad();
                loadStage();
            }
        }
    }

    /**
     * Called on the game end, used for cleaning resources etc.
     */
    public void onExit() {
        log.info("cleaning resources before end");
    }
}
