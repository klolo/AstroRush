package com.astro.core.observe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import common.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(GdxTestRunner.class)
public class KeyObserveTest {

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
        Gdx.input = mock(Input.class);
        when(Gdx.input.isKeyPressed(Input.Keys.APOSTROPHE)).thenReturn(true);
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
        Gdx.input = mock(Input.class);
        when(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)).thenReturn(true);
        observe.handleInput();

        //then
        Assert.assertTrue("Should call observer", observer.isCalled);
        Assert.assertTrue("Should call observer with correct key", observer.calledKey == Input.Keys.ESCAPE);
    }


}
