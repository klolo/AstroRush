package com.astro.core.objects;

import com.astro.core.engine.PhysicsWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class LabelObject extends GameObject {

    @Getter
    private BitmapFont font;

    @Setter
    private String text = "";

    private ShaderProgram fontShader;

    public LabelObject(BitmapFont font) {
        this.font = font;
        fontShader = new ShaderProgram(Gdx.files.internal("font.vert"), Gdx.files.internal("font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }
    }

    @Override
    public void render(OrthographicCamera cam, float delta) {
        batch.setProjectionMatrix(cam.projection);
        batch.setShader(fontShader);

        font.draw(batch, text,
                sprite.getX()*PhysicsWorld.instance.getPIXEL_PER_METER(),
                sprite.getY()*PhysicsWorld.instance.getPIXEL_PER_METER());

        batch.setShader(null);
        batch.setProjectionMatrix(cam.combined);
    }

}
