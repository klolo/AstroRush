package com.astro.core.script.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * Performing scheduled task.
 */
@Slf4j
public class LogicTimer<T> {

    /**
     * data dor scheduled method invoke.
     */
    @Setter
    private T timeoutData;

    /**
     * Increasing time of working
     */
    @Setter
    @Getter
    private float currentTime = 0.0f;

    /**
     * Time limit of the task.
     */
    @Getter
    private final float maxTime;

    /**
     * Method for invoke.
     */
    @Setter
    private Consumer<T> timeoutConsumer;

    /**
     * Stoping the timer.
     */
    @Setter
    private boolean isStopped;

    /**
     * Flag of the looping.
     */
    @Setter
    private boolean looped = true;


    public LogicTimer(final Consumer<T> timeoutConsumer, final T timeoutConsumerParameter, float time) {
        maxTime = time;
        this.timeoutData = timeoutConsumerParameter;
        this.timeoutConsumer = timeoutConsumer;

    }

    public void update(final float delta) {
        if (isStopped) {
            return;
        }

        currentTime += delta;

        if (currentTime >= maxTime) {
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
