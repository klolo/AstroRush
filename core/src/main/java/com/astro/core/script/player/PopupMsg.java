package com.astro.core.script.player;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.storage.GameResources;
import com.astro.core.objects.LabelObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Showing small messages above the player.
 */
public class PopupMsg {

    private String currentMsg = "Lets start!";

    private Queue<String> messagesQueue = new LinkedList<>();

    private float currentMsgTime = 0.0f;

    //TODO: move to properties
    private float SHOW_TIME = 3.0f;

    private float opacity = 1.0f;

    private final int MAX_MSG_ELEMENTS = 3;

    /**
     * Amount of the pixel per meter.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    private LabelObject labelObject;

    public PopupMsg() {
        PropertyInjector.instance.inject(this);

        labelObject = new LabelObject(
                GameResources.instance.getResourceManager().getBitmapFont(LabelObject.getDEFAULT_FONT(),
                        30));

        labelObject.getData().getSprite().setPosition(
                -1 * (Gdx.graphics.getWidth() / (2 * PIXEL_PER_METER)),
                -1 * (Gdx.graphics.getHeight() / (2 * PIXEL_PER_METER))
        );
        labelObject.setScreenPositionRelative(false);
    }

    /**
     * Add messages to queue for later rendering.
     *
     * @param msg - new message
     */
    public void addMessagesToQueue(final String msg) {
        if (currentMsg == null) {
            currentMsg = msg;
            currentMsgTime = 0.0f;
            opacity = 1.0f;
        }

        if (messagesQueue.size() >= MAX_MSG_ELEMENTS) {
            messagesQueue.poll();
        }
        currentMsgTime *= 2;
        messagesQueue.add(msg);
    }

    public void setPos(float x, float y) {
        labelObject.getData().getSprite().setPosition(x, y);
    }

    public void show(final OrthographicCamera cam, float delta) {
        if (currentMsg != null) {
            labelObject.setText(currentMsg);

            Color currentColor = Color.YELLOW;
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
    public void update(float diff) {
        currentMsgTime += diff;
        opacity = 1 - currentMsgTime / SHOW_TIME;

        if (currentMsgTime > SHOW_TIME || opacity < 0.3f) {
            currentMsg = messagesQueue.poll();
            currentMsgTime -= 1.0f;
            opacity = 1.0f;
        }

    }
}
