package com.astro.core.observe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Predicate;

/**
 * Observer for key event, which send event to every registered listener.
 */
@Component
public class KeyObserve {

    private final Predicate<Integer> shouldBeArrowProceed = keyCode -> Gdx.input.isKeyPressed(keyCode);

    private final Predicate<Integer> shouldBeNonArrowProceed = keyCode -> Gdx.input.isKeyJustPressed(keyCode);

    /**
     * Availbale in game keys (from JavaFX). Any other will be ignore.
     */
    private int[] availKeys = {
            Input.Keys.UP,
            Input.Keys.SPACE,
            Input.Keys.DOWN,
            Input.Keys.RIGHT,
            Input.Keys.LEFT,
            Input.Keys.ESCAPE,
            Input.Keys.ENTER,
            Input.Keys.SHIFT_LEFT,
            Input.Keys.TAB
    };

    /**
     * List of registered listeners.
     */
    private LinkedList<IKeyObserver> registerObserve = new LinkedList<>();

    /**
     * Mapping default key code to user settings.
     */
    private HashMap<Integer, Integer> keyMapping;

    /**
     * Register new listener.
     *
     * @param observe - implemetation of the observe interface.
     */
    public void register(final IKeyObserver observe) {
        if (!registerObserve.contains(observe)) {
            registerObserve.add(observe);
        }
    }

    /**
     * Unregister new listener.
     *
     * @param observe - implemetation of the observe interface.
     */
    public void unregister(final IKeyObserver observe) {
        if (observe != null) {
            registerObserve.remove(observe);
        }
    }

    /**
     * If use did set key mapping, in this fuction it will be mapped.
     *
     * @param keyCode - key kode from library.
     */
    private int mapKey(final int keyCode) {
        if (keyMapping == null) {
            return keyCode;
        }

        return keyMapping.get(keyCode);
    }

    /**
     * Called in main loop. In this method we check for key event.
     * For avoid: Exception in thread "LWJGL Application" java.common.ConcurrentModificationException
     * when switching game, firstly i create copy of the array for protection in loop.
     */
    public void handleInput() {
        @SuppressWarnings("unchecked")
        final LinkedList<IKeyObserver> registerObserveCopy = (LinkedList<IKeyObserver>) registerObserve.clone();
        Arrays.stream(availKeys)
                .filter(this::shouldBeKeyProcessed)
                .forEach(keyCode ->
                        registerObserveCopy.iterator().forEachRemaining(o -> o.keyPressEvent(mapKey(keyCode)))
                );
    }

    private boolean shouldBeKeyProcessed(final int keyCode) {
        if (isArrowKey(keyCode)) {
            return shouldBeArrowProceed.test(keyCode);
        } else {
            return shouldBeNonArrowProceed.test(keyCode);
        }
    }

    private boolean isArrowKey(final int keyCode) {
        switch (keyCode) {
            case Input.Keys.RIGHT:
            case Input.Keys.LEFT:
            case Input.Keys.UP:
            case Input.Keys.DOWN: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}

