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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Represent text on the screen loaded from json map.
 */
@Component
@Scope("prototype")
@Slf4j
public class LabelObject extends TextureObject {


    private boolean useShader;

    @Getter
    @Dispose
    private BitmapFont font;

    @Setter
    private String text = "";

    /**
     * If you have null when loading font check at ResourceManager.getBitmapFont,
     * if there are this size.
     */
    @Getter
    static String DEFAULT_FONT = "SF Atarian System";

    @Getter
    static int DEFAULT_SIZE = 30;

    @Dispose
    private ShaderProgram fontShader;

    public void setBitmapFont(final BitmapFont font) {
        this.font = font;

        if (useShader) {
            fontShader = new ShaderProgram(Gdx.files.internal("shaders/font.vert"), Gdx.files.internal("shaders/font.frag"));
            if (!fontShader.isCompiled()) {
                Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
            }
        }
    }

    public void setNationalizedMsg() {
        final String msgVars = data.customVariables.get("msg");
        if (msgVars != null && !"".equals(msgVars)) {
            final String newText = PropertiesReader.instance.getMsg(msgVars);
            if (newText != null && !"".equals(newText)) {
                text = newText;
            }
        }
    }

    @Override
    protected void render(final OrthographicCamera cam, final float delta) {
        batch.setProjectionMatrix(screenPositionRelative ? cam.projection : cam.combined);

        if (useShader) {
            batch.setShader(fontShader);
        }

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(data.getSprite().getColor());
        font.draw(batch, text,
                data.sprite.getX() * pixelPerMeter,
                data.sprite.getY() * pixelPerMeter);

        if (useShader) {
            batch.setShader(null);
        }
    }
}
