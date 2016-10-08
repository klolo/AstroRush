package com.astro.core.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TextureObjectTest {

    @Mock
    private TextureObject textureObjectMock;

    @Mock
    private TextureRegion textureRegionMock;

    @Mock
    private SpriteBatch spriteBatchMock;

    @Before
    public void before() {
        doCallRealMethod().when(textureObjectMock).setBatch(spriteBatchMock);
        doCallRealMethod().when(textureObjectMock).show(null, 0);
        doCallRealMethod().when(textureObjectMock).render(null, 0);
        doCallRealMethod().when(textureObjectMock).setData(null);


        textureObjectMock.setBatch(spriteBatchMock);
        textureObjectMock.data = new ObjectData();
    }

    @Test
    public void shouldNotRenderObjectWhenRenderingIsInScript() {
        doCallRealMethod().when(textureObjectMock).setRenderingInScript(true);

        doThrow(new IllegalArgumentException())
                .when(textureObjectMock).prepareBatch(null);

        textureObjectMock.setRenderingInScript(true);
        textureObjectMock.show(null, 0);
    }

    @Test
    public void shouldRenderInWindowMode() {
        doNothing().when(textureObjectMock).drawTextureRegionFullscreen(0f, 0f);
        doThrow(new IllegalArgumentException())
                .when(textureObjectMock).drawTextureRegion(0, 0);

        textureObjectMock
                .data
                .customVariables
                .put(textureObjectMock.DISPLAY_MODE_KEY, textureObjectMock.FULLSCREEN_MODE);

        //then
        textureObjectMock.render(null, 0);
    }

    @Test
    public void shouldRenderInFullscreenMode() {
        doNothing().when(textureObjectMock).drawTextureRegion(0f, 0f);
        doThrow(new IllegalArgumentException())
                .when(textureObjectMock).drawTextureRegionFullscreen(0, 0);

        textureObjectMock
                .data
                .customVariables
                .clear();


        //then
        textureObjectMock.render(null, 0);
    }
}
