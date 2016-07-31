package com.astro.core.engine.base;

import com.astro.core.engine.interfaces.IGameLogic;
import com.astro.core.engine.stage.GameStage;
import com.astro.core.engine.stage.Stage;
import com.astro.core.engine.stage.StageConfig;
import com.astro.core.engine.stage.StageFactory;
import com.astro.core.script.stage.IStageLogic;
import com.badlogic.gdx.Gdx;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class GameLogic implements IGameLogic {

    private Stage currentStage = Stage.MAIN_MENU;

    @Getter
    private GameStage currentScreen;

    private GameStage prevScreen;

    HashMap<Stage, GameStage> loadedStages = new HashMap<>();

    @Setter
    private Map<String, StageConfig> screenConfigs = new HashMap<>();

    @Setter
    @Autowired
    StageFactory stageFactory;

    public void onExit() {
        log.info("cleaning resources before end");
        unregisterScreen(currentScreen);
    }

    public void render(final float deltaTime) {
        currentScreen.render(deltaTime);
    }

    public void update(final float time) {
        currentScreen.update(time);
        processEvent();
    }

    void processEvent() {
        Optional.of(currentScreen)
                .map(GameStage::getStageLogic)
                .map(IStageLogic::getEvent)
                .ifPresent(event -> {
                    switch (event) {
                        case SWITCH_STAGE: {
                            switchStage();
                            break;
                        }
                        case NEW_STAGE: {
                            newStage();
                            break;
                        }
                        case RESUME: {
                            resume();
                            break;
                        }
                        case GAME_EXIT: {
                            Gdx.app.exit();
                            break;
                        }
                    }

                    currentScreen.getStageLogic().setEvent(null);
                });
    }

    public void switchStage() {
        Stage stageToLoad = Stage.MAIN_MENU;

        if (currentScreen != null) {
            stageToLoad = currentScreen.getStageLogic().getStageToLoad();
            unregisterScreen(currentScreen);
        }

        log.info("switch stage to: {}", stageToLoad);

        if (prevScreen != null) {
            prevScreen = currentScreen;
            prevScreen.unregisterPhysics();
        }

        currentStage = stageToLoad;

        if (stageToLoad != null && loadedStages.containsKey(stageToLoad)) {
            currentScreen = loadedStages.get(stageToLoad);
            currentScreen.register();
        }
        else {
            currentScreen = stageFactory.create(screenConfigs.get(currentStage.toString()));
            loadedStages.put(stageToLoad, currentScreen);
        }
    }

    private void newStage() {
        log.info("start");

        if (prevScreen != null) {
            prevScreen.unregisterPhysics();
            prevScreen = currentScreen;
        }

        destroyLevelStage();

        currentScreen = stageFactory.create(screenConfigs.get(Stage.LEVEL1.toString()));
        loadedStages.put(Stage.LEVEL1, currentScreen);
        currentScreen.register();
    }

    private void resume() {
        log.info("start");
        unregisterScreen(currentScreen);
        currentScreen = loadedStages.get(Stage.LEVEL1);
        currentScreen.register();
    }

    private void unregisterScreen(final GameStage stage) {
        Optional.ofNullable(stage).ifPresent(GameStage::unregister);
    }

    private void destroyLevelStage() {
        Optional.ofNullable(loadedStages.get(Stage.LEVEL1)).ifPresent(GameStage::destroy);
    }

    public GameStage getGameScreen() {
        return currentScreen;
    }
}
