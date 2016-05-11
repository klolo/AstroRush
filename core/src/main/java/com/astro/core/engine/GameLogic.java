package com.astro.core.engine;

import com.astro.core.engine.stage.*;
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


    /**
     * TODO: doc
     */
    private Stages currentStage = Stages.MAIN_MENU; // from json file stages file

    /**
     * TODO: doc
     */
    private HashMap<Stages, StageConfig> screenConfigs = new HashMap<>();

    /**
     * TODO: doc
     */
    private GameStage currentScreen;

    /**
     * TODO: doc
     */
    private StageConfigReader configReader = new StageConfigReader();

    /**
     * TODO: doc
     */
    private Game game;

    /**
     * TODO: doc
     */
    public GameStage getGameScreen() {
        return currentScreen;
    }

    public void init() {
        log.info("AstroGame init");
        KeyObserve.instance.register(this);

        Arrays.asList(configReader.getConfigs())
                .stream()
                .collect(Collectors.toList())
                .forEach(s -> screenConfigs.put(currentStage.mapJsonNameToEnumVal(s.stageName), s));

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
