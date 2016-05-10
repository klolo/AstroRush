package com.astro.core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represent text on the screen loaded from json map.
 */
@Slf4j
public class LabelObject extends TextureObject {

    /**
     * Text is relative to screen or world?
     */
    @Setter
    private boolean screenPositionRelative = false;


    private boolean useShader = false;

    @Getter
    private BitmapFont font;

    @Setter
    private String text = "";

    //TODO: move to properties
    /**
     * If you have null when loading font check at ResourceManager.getBitmapFont,
     * if there are this size.
     */
    @Getter
    private static String DEFAULT_FONT = "StayPuft";

    //TODO: move to properties
    @Getter
    private static int DEFAULT_SIZE = 30;

    /**
     * TODO: doc
     */
    private ShaderProgram fontShader;

    public LabelObject(BitmapFont font) {
        this.font = font;

        if (useShader) {
            fontShader = new ShaderProgram(Gdx.files.internal("font.vert"), Gdx.files.internal("font.frag"));
            if (!fontShader.isCompiled()) {
                Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
            }
        }
    }

    @Override
    protected void render(OrthographicCamera cam, float delta) {
        if (screenPositionRelative) {
            batch.setProjectionMatrix(cam.projection);
        }
        else {
            batch.setProjectionMatrix(cam.combined);
        }

        if (useShader) {
            batch.setShader(fontShader);
        }

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.draw(batch, text,
                sprite.getX() * PIXEL_PER_METER,
                sprite.getY() * PIXEL_PER_METER);

        if (useShader) {
            batch.setShader(null);
        }

        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        fontShader.dispose();
    }

}
