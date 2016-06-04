package com.astro.core.engine.base;

import com.astro.core.engine.interfaces.IGameLogic;
import com.astro.core.engine.stage.*;
import com.astro.core.script.stage.IStageLogic;
import com.badlogic.gdx.Gdx;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class GameLogic implements IGameLogic {

    private Stage currentStage = Stage.MAIN_MENU;

    @Getter
    private GameStage currentScreen;

    private GameStage prevScreen;

    private HashMap<Stage, GameStage> loadedStages = new HashMap<>();

    private HashMap<Stage, StageConfig> screenConfigs = new HashMap<>();

    private StageConfigReader configReader = new StageConfigReader();

    public void init() {
        log.info("start");

        Arrays.asList(configReader.getConfigs())
                .stream()
                .collect(Collectors.toList())
                .forEach(s -> screenConfigs.put(Stage.valueOf(s.stageName), s));

        loadStage();
    }

    public GameStage getGameScreen() {
        return currentScreen;
    }

    public void onExit() {
        log.info("cleaning resources before end");
        unregisterScreen(currentScreen);
    }

    public void render(float deltaTime) {
        currentScreen.render(deltaTime);
    }

    public void update(float time) {
        currentScreen.update(time);
        processEvent();
    }

    private void processEvent() {
        Optional.of(currentScreen)
                .map(GameStage::getStageLogic)
                .map(IStageLogic::getEvent)
                .ifPresent(event -> {
                    currentScreen.getStageLogic().setEvent(null);

                    switch (event) {
                        case GAME_EXIT: {
                            Gdx.app.exit();
                            break;
                        }
                        case SWITCH_STAGE: {
                            loadStage();
                            break;
                        }
                        case PREV_STAGE: {
                            prevStage();
                            break;
                        }
                        case NEW_STAGE: {
                            newStage();
                            break;
                        }
                        case RESUME: {
                            prevStage();
                            break;
                        }
                    }
                });
    }

    private void newStage() {
        log.info("new stage");
        prevScreen.unregisterPhysics();
        prevScreen = currentScreen;

        destroyLevelStage();

        currentScreen = StageFactory.instance.create(screenConfigs.get(Stage.LEVEL1));
        loadedStages.put(Stage.LEVEL1, currentScreen);
        currentScreen.register();
    }

    private void destroyLevelStage() {
        GameStage gameStage = loadedStages.get(Stage.LEVEL1);
        if (gameStage != null) {
            gameStage.unregister();
            gameStage.unregisterPhysics();
        }
    }

    private void prevStage() {
        log.info("prev stage");
        if (prevScreen == null) {
            log.warn("Prev stage does not exist");
            return;
        }

        GameStage currentStageTmp = currentScreen;
        currentScreen = prevScreen;

        prevScreen = currentStageTmp;
        unregisterScreen(prevScreen);

        currentScreen.register();
    }

    private void loadStage() {
        log.info("load stage");
        Stage stageToLoad = Stage.MAIN_MENU;

        if (currentScreen != null) {
            prevScreen = currentScreen;
            unregisterScreen(currentScreen);

            stageToLoad = currentScreen.getStageLogic().getStageToLoad();
            currentStage = stageToLoad;
        }

        if (stageToLoad != null && loadedStages.containsKey(stageToLoad)) {
            currentScreen = loadedStages.get(stageToLoad);
            currentScreen.register();
        }
        else {
            currentScreen = StageFactory.instance.create(screenConfigs.get(currentStage));
            loadedStages.put(stageToLoad, currentScreen);
        }
    }

    private void unregisterScreen(final GameStage stage) {
        if (stage != null) {
            stage.unregister();
        }
    }

}
