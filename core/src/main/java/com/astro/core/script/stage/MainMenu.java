package com.astro.core.script.stage;

import com.astro.core.engine.base.GameEvent;
import com.astro.core.engine.stage.Stage;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Represents logic of the MainMenu.
 */
@Slf4j
public class MainMenu extends StageLogic {

    @Autowired
    private ObjectsRegister objectsRegister;

    private final String PREFIX = "mnBtn_";

    private int currentActiveBtn = 0;

    private final String EXIT_BUTTON = "EXIT";

    private final String RESUME_BUTTON = "RESUME";

    @Getter
    private boolean isResumeActive = false;

    @Autowired
    private KeyObserve keyObserve;

    private boolean isGameStarted = false;

    private final String[] buttons = {
            RESUME_BUTTON,
            Stage.LEVEL1.toString(),
            Stage.SETTINGS.toString(),
            Stage.HIGHSCORE.toString(),
            EXIT_BUTTON
    };

    @Override
    public void init() {
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
        else if (buttons[currentActiveBtn].equals(Stage.LEVEL1.toString())) {
            if (isResumeActive) {
                event = GameEvent.NEW_STAGE;
                isGameStarted = true;
            }
            else {
                event = GameEvent.SWITCH_STAGE;
                stageToLoad = Stage.valueOf(buttons[currentActiveBtn]);
                isResumeActive = true;
            }
        }
        else if (buttons[currentActiveBtn].equals(RESUME_BUTTON)) {
            event = GameEvent.RESUME;
        }
        else {
            event = GameEvent.SWITCH_STAGE;
            stageToLoad = Stage.valueOf(buttons[currentActiveBtn]);
        }
    }

    void processArrowDown() {
        if (currentActiveBtn != buttons.length - 1) {
            setColorOnActiveButton(Color.WHITE, PREFIX + buttons[currentActiveBtn]);
            setColorOnActiveButton(Color.YELLOW, PREFIX + buttons[++currentActiveBtn]);
        }
        log.info("Active button: {}", buttons[currentActiveBtn]);
    }

    private void processArrowUp() {
        if (currentActiveBtn == 1 && isResumeActive() || currentActiveBtn > 1) {
            setColorOnActiveButton(Color.WHITE, PREFIX + buttons[currentActiveBtn]);
            setColorOnActiveButton(Color.YELLOW, PREFIX + buttons[--currentActiveBtn]);
        }

        log.info("Active button: {}", buttons[currentActiveBtn]);
    }

    /**
     * Changing color of the menu label.
     * If label do not have id will be null pointer, you should set it in editor.
     */
    private void setColorOnActiveButton(final Color color, final String objectID) {
        final IGameObject objects = objectsRegister.getObjectByID(objectID);
        if (objects != null) {
            objects.getData().getSprite().setColor(color);
        }
        else {
            log.info("Not found object");
        }
    }

    @Override
    public void onResume() {
        keyObserve.register(this);

        selectectResumeButton();

        if (isGameStarted) {
            isResumeActive = true;
        }

        setColorOnActiveButton(Color.YELLOW, PREFIX + buttons[currentActiveBtn]);
        setColorOnActiveButton(Color.WHITE, PREFIX + buttons[currentActiveBtn + 1]);
    }

    private void selectectResumeButton() {
        if (buttons[currentActiveBtn].equals(Stage.LEVEL1.toString())) {
            currentActiveBtn = 0;
        }
    }
}
