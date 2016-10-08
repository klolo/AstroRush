package com.astro.core.mock;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Mock of the all libgdx staffs. Inherit this class and  call mehods when you need
 * check something dependend of gdx.
 */
public class GdxMock {

    public GdxMock() {
        mockInput();
    }

    protected void mockInput() {
        Gdx.input = mock(Input.class);
    }

    protected void pressKey(final int keyCode) {
        mockInput();
        when(Gdx.input.isKeyPressed(keyCode)).thenReturn(true);
    }

}
