package com.astro.game.observe;

import com.astro.game.mock.GdxMock;
import com.badlogic.gdx.Input;
import org.junit.Assert;
import org.junit.Test;


public class KeyObserveTest extends GdxMock {

    class Observer implements IKeyObserver {
        boolean isCalled;

        int calledKey;

        @Override
        public void keyPressEvent(final int keyCode) {
            isCalled = true;
            calledKey = keyCode;
        }
    }

    @Test
    public void shouldNotWorkForUnsupportedKey() {
        //given
        final Observer observer = new Observer();
        final KeyObserve observe = new KeyObserve();
        observe.register(observer);

        //when
        pressKey(Input.Keys.APOSTROPHE);
        observe.handleInput();

        //then
        Assert.assertFalse("Should not call observer", observer.isCalled);
    }

    @Test
    public void shouldCallObserverForSupportedKey() {
        //given
        final Observer observer = new Observer();
        final KeyObserve observe = new KeyObserve();
        observe.register(observer);

        //when
        pressKey(Input.Keys.ESCAPE);
        observe.handleInput();

        //then
        Assert.assertTrue("Should call observer", observer.isCalled);
        Assert.assertTrue("Should call observer with correct key", observer.calledKey == Input.Keys.ESCAPE);
    }


}
