package com.astro.core.script.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * Created by kamil on 17.05.16.
 */
@Slf4j
public class LogicTimer<T> {

    @Setter
    T timeoutData;

    @Setter
    private float currentTime = 0.0f;

    @Getter
    private final float maxTime;

    @Setter
    private Consumer<T> timeoutConsumer;

    @Setter
    private boolean isStopped = false;

    @Setter
    private boolean looped = true;

    /**
     * @param timeoutData
     * @param timeoutConsumer
     * @param time
     */
    public LogicTimer(final T timeoutData, final Consumer<T> timeoutConsumer, float time) {
        maxTime = time;
        this.timeoutData = timeoutData;
        this.timeoutConsumer = timeoutConsumer;

    }

    public void update(float delta) {
        if (isStopped) {
            return;
        }

        currentTime += delta;

        if (currentTime > maxTime) {
            if (!looped) {
                isStopped = true;
            }
            currentTime = 0.0f;

            log.debug("Timeout");
            timeoutConsumer.accept(timeoutData);
        }
    }

    public void reset() {
        currentTime = 0.0f;
    }

}
