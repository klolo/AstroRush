package com.astro.core.logic.common;

import com.astro.core.objects.LabelObject;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * Showing small messages above the player.
 */
@Component
@Scope("prototype")
@Slf4j
public class PopupMsg implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Value("${player.dialog.start}")
    @Setter
    private String currentMsg;

    @Value("${renderer.pixel.per.meter}")
    protected short pixelPerMeter;

    @Getter
    public LinkedList<String> messagesQueue = new LinkedList<>();

    public float currentMsgTime;

    private float opacity = 1.0f;

    private static final byte MAX_MSG_ELEMENTS = 3;

    private LabelObject labelObject;

    @Setter
    private boolean instantAdd;

    public void initLabel() {
        labelObject = applicationContext.getBean(LabelObject.class);

        labelObject.setBitmapFont(
                GameResources.instance.getResourceManager().getBitmapFont(LabelObject.getDEFAULT_FONT(), 30));

        labelObject.getData().getSprite().setPosition(
                -1 * (Gdx.graphics.getWidth() / (2 * pixelPerMeter)),
                -1 * (Gdx.graphics.getHeight() / (2 * pixelPerMeter))
        );

        labelObject.setScreenPositionRelative(false);
    }

    public void addMessagesToQueue(final String msg) {
        for (final String currentMessage : messagesQueue) {
            if (currentMessage.equals(msg)) {
                return;
            }
        }

        if (messagesQueue.size() > MAX_MSG_ELEMENTS) {
            return;
        }

        if (currentMsg == null || instantAdd) {
            messagesQueue.add(msg);
            currentMsg = msg;
            currentMsgTime = 0.0f;
            opacity = 1.0f;
        }
    }

    public void setPosWithCenter(final float x, final float y) {
        labelObject.getData().getSprite().setPosition(x, y);
    }

    public void show(final OrthographicCamera cam, final float delta) {
        if (currentMsg == null) {
            return;
        }
        labelObject.setText(currentMsg);

        final Color currentColor = Color.YELLOW;
        labelObject.getData().getSprite().setColor(
                currentColor.r,
                currentColor.g,
                currentColor.b,
                opacity);
        labelObject.show(cam, delta);
    }

    public void update(final float diff) {
        if (currentMsg == null) {
            return;
        }
        currentMsgTime += diff;
        opacity -= 1f / 60f;

        LOGGER.info("O::: {}", opacity);

        if (opacity < 0.1) {
            showNextMessage();
        }
    }

    private void showNextMessage() {
        currentMsg = null;
        currentMsgTime = 0.0f;
        opacity = 1.0f;

        if (messagesQueue.size() > 0) {
            currentMsg = messagesQueue.get(0);
            messagesQueue.remove(0);
        }
    }
}
