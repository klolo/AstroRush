package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Represents a drawable on screen texture object.
 */
@Slf4j
@Component
@Scope("prototype")
public class TextureObject extends GameObject {

    static final String DISPLAY_MODE_KEY = "displaymode";

    static final String FULLSCREEN_MODE = "fullscreen";

    /**
     * Text is relative to screen or world?
     */
    @Setter
    protected boolean screenPositionRelative;

    @Getter
    protected TextureRegion textureRegion;

    /**
     * Batch using for rendering object.
     */
    @Dispose
    @Getter
    @Setter
    protected Batch batch;

    public TextureObject(final SpriteBatch spriteBatch) {
        this.batch = spriteBatch;
        batch.enableBlending();
    }

    public TextureObject() {
        batch = new SpriteBatch();
        batch.enableBlending();
    }

    /**
     * Default constructor.
     */
    public void setTextureRegion(final TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        data.sprite = new Sprite(textureRegion);
    }

    /**
     * Every object should be rendered by this method.
     */
    public void show(final OrthographicCamera cam, final float delta) {
        if (renderingInScript) {
            return;
        }

        prepareBatch(cam);
        render(cam, delta);
        batch.end();
    }

    public void showFromLogic(final OrthographicCamera cam, final float delta) {
        prepareBatch(cam);
        render(cam, delta);
        batch.end();
    }

    public void prepareBatch(final OrthographicCamera cam) {
        batch.setProjectionMatrix(screenPositionRelative ? cam.projection : cam.combined);
        batch.begin();
        batch.setColor(data.sprite.getColor());

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(final OrthographicCamera cam, final float delta) {
        final String displayMode = data.customVariables.getOrDefault(DISPLAY_MODE_KEY, "");
        switch (displayMode) {
            case FULLSCREEN_MODE: {
                drawTextureRegionFullscreen(0, 0);
                break;
            }
            default: {
                final float x = data.sprite.getX();
                final float y = data.sprite.getY();
                final float px = x + data.sprite.getOriginX();
                final float py = y + data.sprite.getOriginY();

                final float pX = getPx(px, data.sprite.getWidth());
                final float pY = getPy(py, data.sprite.getHeight());

                drawTextureRegion(pX, pY);
                break;
            }
        }
    }

    /**
     * Get X position of the bottom left corner of object.
     */
    float getPx(final float x, final float width) {
        final float result = x * pixelPerMeter;
        final float halfWith = width * data.sprite.getScaleX() / 2;
        return result - halfWith;
    }

    /**
     * Get Y position of the bottom left corner of object.
     */
    float getPy(final float y, final float height) {
        return y * pixelPerMeter - height * data.sprite.getScaleY() / 2;
    }

    /**
     * Drawing images in fullscreen mode.
     */
    void drawTextureRegionFullscreen(final float pX, final float pY) {
        batch.draw(
                textureRegion.getTexture(),
                pX - Gdx.graphics.getWidth() / 2,
                pY - Gdx.graphics.getHeight() / 2,
                data.sprite.getOriginX(),
                data.sprite.getOriginY(),
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                1,
                1,
                data.sprite.getRotation(),
                textureRegion.getRegionX(),
                textureRegion.getRegionY(),
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(),
                data.flipX,
                data.flipY
        );
    }

    /**
     * Draws image on the screen.
     */
    void drawTextureRegion(final float pX, final float pY) {
        batch.draw(
                textureRegion.getTexture(),
                pX,
                pY,
                data.sprite.getOriginX(),
                data.sprite.getOriginY(),
                data.sprite.getWidth(),
                data.sprite.getHeight(),
                data.sprite.getScaleX(),
                data.sprite.getScaleY(),
                data.sprite.getRotation(),
                textureRegion.getRegionX(),
                textureRegion.getRegionY(),
                textureRegion.getRegionWidth(),
                textureRegion.getRegionHeight(),
                data.flipX,
                data.flipY
        );
    }
}
