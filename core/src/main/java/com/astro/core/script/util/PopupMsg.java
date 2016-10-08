package com.astro.core.script.util;

import com.astro.core.objects.LabelObject;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Showing small messages above the player.
 */
@Component
@Scope("prototype")
public class PopupMsg implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;
    /**
     * Player starting popup.
     */
    @Value("${player.dialog.start}")
    @Setter
    private String currentMsg;

    /**
     * Time of the showing popup.
     */
    @Value("${player.popup.time}")
    private float SHOW_TIME;

    /**
     * Amount of the pixel per meter.
     */
    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter;

    /**
     * Queue of the messages.
     */
    @Getter
    public LinkedList<String> messagesQueue = new LinkedList<>();

    public float currentMsgTime;

    private float opacity = 1.0f;

    private static final int MAX_MSG_ELEMENTS = 3;

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

    /**
     * Add messages to queue for later rendering.
     *
     * @param msg - new message
     */
    public void addMessagesToQueue(final String msg) {
        if (currentMsg == null || instantAdd) {
            currentMsg = msg;
            currentMsgTime = 0.0f;
            opacity = 1.0f;
            return;
        }

        if (Collections.frequency(messagesQueue, msg) > MAX_MSG_ELEMENTS) {
            return;
        }

        if (!messagesQueue.isEmpty() && messagesQueue.element().equals(msg)) {
            return;
        }

        if (messagesQueue.isEmpty() && msg.equals(currentMsg)) {
            return;
        }

        if (msg.equals(currentMsg)) {
            return;
        }

        messagesQueue.add(msg);
    }

    public void setPos(final float x, final float y) {
        labelObject.getData().getSprite().setPosition(x, y); //fixme: should be centered above the player
    }

    public void show(final OrthographicCamera cam, final float delta) {
        if (currentMsg != null) {
            labelObject.setText(currentMsg);

            final Color currentColor = Color.YELLOW;
            labelObject.getData().getSprite().setColor(
                    currentColor.r,
                    currentColor.g,
                    currentColor.b,
                    opacity);
            labelObject.show(cam, delta);
        }
    }

    /**
     *
     */
    public void update(final float diff) {
        currentMsgTime += diff;
        opacity = 1 - currentMsgTime / SHOW_TIME;

        if (currentMsgTime > SHOW_TIME || opacity < 0.1f) {
            currentMsg = messagesQueue.poll();
            currentMsgTime = 0.0f;
            opacity = 1.0f;
        }
    }
}
