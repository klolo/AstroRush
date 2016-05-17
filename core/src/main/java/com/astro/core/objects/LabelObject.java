package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.astro.core.storage.PropertiesReader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
    @Dispose
    private BitmapFont font;

    @Setter
    private String text = "";

    //TODO: move to properties
    /**
     * If you have null when loading font check at ResourceManager.getBitmapFont,
     * if there are this size.
     */
    @Getter
    private static String DEFAULT_FONT = "SF Atarian System";

    //TODO: move to properties
    @Getter
    private static int DEFAULT_SIZE = 30;

    /**
     * TODO: doc
     */
    @Dispose
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

    public void setNationalizedMsg() {
        String msgVars = data.customVariables.get("msg");
        if (msgVars != null && !"".equals(msgVars)) {
            String newText = PropertiesReader.instance.getMsg(msgVars);
            if (newText != null && !"".equals(newText)) {
                text = newText;
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
        font.setColor(data.getSprite().getColor());
        font.draw(batch, text,
                data.sprite.getX() * PIXEL_PER_METER,
                data.sprite.getY() * PIXEL_PER_METER);

        if (useShader) {
            batch.setShader(null);
        }

        batch.setProjectionMatrix(cam.combined);
    }
}
