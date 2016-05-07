package com.astro.core.objects;

import com.astro.core.engine.PhysicsWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
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

    private ShaderProgram fontShader;

    public LabelObject(BitmapFont font) {
        this.font = font;

        if(useShader) {
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

        if(useShader) {
            batch.setShader(fontShader);
        }

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.draw(batch, text,
                sprite.getX() * PhysicsWorld.instance.PIXEL_PER_METER,
                sprite.getY() * PhysicsWorld.instance.PIXEL_PER_METER);

        if(useShader) {
            batch.setShader(null);
        }

        batch.setProjectionMatrix(cam.combined);
    }

}
