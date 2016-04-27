package com.astro.core.observe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Observer for key event, which send event to every registered listener.
 */
public enum KeyObserve {
    instance;

    /**
     * Availbale in game keys (from JavaFX). Any other will be ignore.
     */
    private int[] availKeys = {
            Input.Keys.UP,
            Input.Keys.DOWN,
            Input.Keys.RIGHT,
            Input.Keys.LEFT,
            Input.Keys.ESCAPE
    };

    /**
     * List of registered listeners.
     */
    private ArrayList<IKeyObserver> registerObserve = new ArrayList<>();

    /**
     * Mapping default key code to user settings.
     */
    private HashMap<Integer, Integer> keyMapping = null;

    /**
     * Register new listener.
     *
     * @param observe - implemetation of the observe interface.
     */
    public void register(IKeyObserver observe) {
        registerObserve.add(observe);
    }

    /**
     * If use did set key mapping, in this fuction it will be mapped.
     *
     * @param keyCode - key kode from library.
     */
    private int mapKey(int keyCode) {
        if (keyMapping == null) {
            return keyCode;
        }

        return keyMapping.get(keyCode);
    }

    /**
     * Called in main loop. In this method we check for key event.
     */
    public void handleInput() {
        Arrays.stream(availKeys)
                .filter(keyCode -> Gdx.input.isKeyPressed(keyCode))
                .filter(keyCode -> ArrayUtils.contains(availKeys, keyCode))
                .forEach(keyCode ->
                        registerObserve.forEach(o -> o.keyPressEvent(mapKey(keyCode)))
                );
    }
}

