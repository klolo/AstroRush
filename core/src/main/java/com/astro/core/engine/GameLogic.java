package com.astro.core.engine;

import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.StageConfig;
import com.astro.core.engine.stage.StageConfigReader;
import com.astro.core.engine.stage.StageFactory;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Represents implementation of the Astro Rush 2.0 game.
 */
@Slf4j
public class GameLogic implements IGameLogic {

    enum Stages {
        MAIN_MENU,
        LEVEL1
    }

    private Stages mapJsonNameToEnumVal(String stageName) {
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

    private Stages currentStage = Stages.MAIN_MENU; // from json file stages file

    private HashMap<Stages, StageConfig> screenConfigs = new HashMap<>();

    private GameStage currentScreen;

    private StageConfigReader configReader = new StageConfigReader();

    private Game game;

    public GameStage getGameScreen() {
        return currentScreen;
    }

    public void init() {
        log.info("AstroGame init");
        KeyObserve.instance.register(this);

        Arrays.asList(configReader.getConfigs())
                .stream()
                .collect(Collectors.toList())
                .forEach(s -> screenConfigs.put(mapJsonNameToEnumVal(s.stageName), s));

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
    }

    /**
     * Called on the game end, used for cleaning resources etc.
     */
    public void onExit() {
        log.info("cleaning resources before end");
    }

    @Override
    public void keyPressEvent(int keyCode) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {

            if (currentStage == Stages.MAIN_MENU) {
                currentStage = Stages.LEVEL1;
            }
            else {
                currentStage = Stages.MAIN_MENU;
            }
            loadStage();
        }
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }

}
