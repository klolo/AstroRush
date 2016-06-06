package com.astro.core.script.player;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.Msg;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.objects.LabelObject;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Showing small messages above the player.
 */
public class PopupMsg {

    /**
     * Player starting popup.
     */
    @Msg("player.dialog.start")
    private String currentMsg;

    /**
     * Time of the showing popup.
     */
    @GameProperty("player.popup.time")
    private float SHOW_TIME = 0.0f;

    /**
     * Queue of the messages.
     */
    private LinkedList<String> messagesQueue = new LinkedList<>();


    private float currentMsgTime = 0.0f;

    private float opacity = 1.0f;

    private final int MAX_MSG_ELEMENTS = 3;

    /**
     * Amount of the pixel per meter.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    private LabelObject labelObject;

    PopupMsg() {
        PropertyInjector.instance.inject(this);
    }

    public void initLabel() {
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
    void addMessagesToQueue(final String msg) {
        if (currentMsg == null) {
            currentMsg = msg;
            currentMsgTime = 0.0f;
            opacity = 1.0f;
        }

        if (Collections.frequency(messagesQueue, msg) > MAX_MSG_ELEMENTS) {
            return;
        }

        if (messagesQueue.size() > 0 && messagesQueue.element().equals(msg)) {
            return;
        }

        if (messagesQueue.size() == 0 && msg.equals(currentMsg)) {
            return;
        }

        messagesQueue.add(msg);
    }

    public void setPos(final float x, final float y) {
        labelObject.getData().getSprite().setPosition(x, y); //fixme: should be centered above the player
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
