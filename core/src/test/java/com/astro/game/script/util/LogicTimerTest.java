package com.astro.game.script.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests of the schedules workers.
 */
public class LogicTimerTest {

    private boolean isDone = false;

    private int counter = 0;

    private void setIsDone(Boolean val) {
        isDone = val;
    }

    private void increaseCounter(int i) {
        counter += i;
    }

    @Test
    public void shouldDone() {
        //given
        LogicTimer watcher = new LogicTimer<>(this::setIsDone, true, 1);

        //when
        watcher.update(1);

        //then
        Assert.assertTrue("Timer should invoke method setIsDone with true param", isDone);
    }

    @Test
    public void shouldTimeNotChange() {
        //given
        LogicTimer watcher = new LogicTimer<>(this::setIsDone, true, 1);

        //when
        watcher.setStopped(true);
        watcher.update(1);

        //then
        Assert.assertTrue("Timer should not work", watcher.getCurrentTime() == 0.0f);
    }

    @Test
    public void shouldCorrectlyIncreaseCounter() {
        //given
        LogicTimer watcher = new LogicTimer<>(this::increaseCounter, 1, 1);

        //when
        watcher.update(1);
        watcher.update(1);

        //then
        Assert.assertTrue("Timer should made 2 cycle", counter == 2);
    }

}
