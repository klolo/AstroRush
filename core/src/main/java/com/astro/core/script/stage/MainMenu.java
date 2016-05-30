package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.Stage;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents logic of the MainMenu.
 */
@Slf4j
public class MainMenu extends StageLogic {

    private final String PREFIX = "mnBtn_";

    private int currentActiveBtn = 0;

    private final String EXIT_BUTTON = "EXIT";

    private final String RESUME_BUTTON = "RESUME";

    private boolean isResumeActive = false;

    private final String[] buttons = {
            RESUME_BUTTON,
            Stage.LEVEL1.toString(),
            Stage.SETTINGS.toString(),
            Stage.HIGHSCORE.toString(),
            EXIT_BUTTON
    };

    /**
     * Object is creating by reflection.
     */
    public MainMenu() {
        currentActiveBtn = 0;
        setColorOnActiveButton(Color.GRAY, PREFIX + buttons[currentActiveBtn]);
        setColorOnActiveButton(Color.YELLOW, PREFIX + buttons[++currentActiveBtn]);
    }

    @Override
    public void keyPressEvent(int keyCode) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            processEnter();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            processArrowDown();
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            processArrowUp();
        }
    }

    void processEnter() {
        if (buttons[currentActiveBtn].equals(EXIT_BUTTON)) {
            event = GameEvent.GAME_EXIT;
        }
        else {
            event = GameEvent.SWITCH_STAGE;
            stageToLoad = Stage.valueOf(buttons[currentActiveBtn]);
            if (stageToLoad == Stage.LEVEL1) {
                isResumeActive = true;
            }
        }
    }

    void processArrowDown() {
        if (currentActiveBtn != buttons.length - 1) {
            setColorOnActiveButton(Color.WHITE, PREFIX + buttons[currentActiveBtn]);
            setColorOnActiveButton(Color.YELLOW, PREFIX + buttons[++currentActiveBtn]);
        }
    }

    void processArrowUp() {
        if (currentActiveBtn != 1 || isResumeActive()) {
            setColorOnActiveButton(Color.WHITE, PREFIX + buttons[currentActiveBtn]);
            setColorOnActiveButton(Color.YELLOW, PREFIX + buttons[--currentActiveBtn]);
        }
    }

    /**
     * TODO:
     */
    private boolean isResumeActive() {
        return isResumeActive;
    }

    /**
     * Changing color of the menu label.
     * If label do not have id will be null pointer, you should set it in editor.
     */
    private void setColorOnActiveButton(final Color color, final String objectID) {
        IGameObject objects = ObjectsRegister.instance.getObjectByID(objectID);
        if (objects != null) {
            objects.getData().getSprite().setColor(color);
        }
        else {
            log.info("Not found object");
        }
    }
}
