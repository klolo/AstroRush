package com.astro.core.converters;

import com.astro.core.objects.GameObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.MainItemVOToIGameObjectConverter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.uwsoft.editor.renderer.data.MainItemVO;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of the  MessagesManager.
 */
public class MainItemVOToIGameObjectConverterTest {

    @Getter
    private MainItemVO input;

    @Getter
    private IGameObject result;

    @Before
    public void init() {
        result = new GameObject() {
            @Override
            protected void render(final OrthographicCamera cam, final float delta) {
            }

            @Override
            public void show(final OrthographicCamera cam, final float delta) {
            }
        };

        result.getData().setSprite(new Sprite());

        input = new MainItemVO();
        input.layerName = "layer";
        input.customVars = "foo:fuu";
        input.itemIdentifier = "ident";
        input.rotation = 90f;
        input.scaleX = 1.0f;
        input.scaleY = 1.0f;
        input.x = 10f;
        input.y = 10f;
    }

    @Test
    public void shouldCorrectConvert() {
        //given
        final MainItemVOToIGameObjectConverter converter = new MainItemVOToIGameObjectConverter();

        //when
        converter.convert(input, result);

        //then
        checkSprite(result.getData().getSprite());
    }

    private void checkSprite(final Sprite resultSprite) {
        Assert.assertTrue("Layer should be rewrite", result.getData().getLayerID().equals(input.layerName));
        Assert.assertTrue("ID should be rewrite", result.getData().getItemIdentifier().equals(input.itemIdentifier));
        Assert.assertTrue("Custom vars should be rewrite", result.getData().getCustomVariables().get("foo").equals("fuu"));
        Assert.assertTrue("rotation should be rewrite", resultSprite.getRotation() == input.rotation);

        Assert.assertTrue("scaleX should be rewrite", resultSprite.getScaleX() == input.scaleX);
        Assert.assertTrue("scaleY should be rewrite", resultSprite.getScaleY() == input.scaleY);
        Assert.assertTrue("x should be rewrite", resultSprite.getX() == input.x);
        Assert.assertTrue("y should be rewrite", resultSprite.getY() == input.y);
    }

}
