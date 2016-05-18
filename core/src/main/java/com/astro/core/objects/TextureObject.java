package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.astro.core.adnotation.processor.PropertyInjector;
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

/**
 * Represents a drawable on screen texture object.
 */
@Slf4j
public class TextureObject extends GameObject {

    @Getter
    @Setter
    protected boolean renderingInScript = false;

    @Getter
    @Setter
    protected TextureRegion textureRegion;

    /**
     * Batch using for rendering object.
     */
    @Dispose
    @Getter
    @Setter
    protected Batch batch;

    public TextureObject() {
        init();
    }

    /**
     * Default constructor.
     */
    public TextureObject(TextureRegion textureRegion) {
        init();
        this.textureRegion = textureRegion;
        data.sprite = new Sprite(textureRegion);
    }

    /**
     * Called in all constructors.
     */
    private void init() {
        PropertyInjector.instance.inject(this);

        batch = new SpriteBatch();
        batch.enableBlending();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(OrthographicCamera cam, float delta) {
        draw();
    }

    /**
     * Every object should be rendered by this method.
     */
    public void show(OrthographicCamera cam, float delta) {
        if (renderingInScript) {
            return;
        }

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.setColor(data.sprite.getColor());

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        render(cam, delta); // implemented in concreted class.
        batch.end();
    }

    /**
     * Called in main loop
     */
    void draw() {
        draw(data.sprite.getX(), data.sprite.getY());
    }

    /**
     * Render method for texture and physic object.
     */
    private void draw(float x, float y) {
        x += data.sprite.getOriginX();
        y += data.sprite.getOriginY();

        float pX = getPx(x, data.sprite.getWidth());
        float pY = getPy(y, data.sprite.getHeight());

        drawTextureRegion(pX, pY);
    }

    /**
     * Get X position of the bottom left corner of object.
     */
    float getPx(float x, float width) {
        float result = x * PIXEL_PER_METER;
        float halfWith = width * data.sprite.getScaleX() / 2;
        return result - halfWith;
    }

    /**
     * Get Y position of the bottom left corner of object.
     */
    float getPy(float y, float height) {
        return y * PIXEL_PER_METER - height * data.sprite.getScaleY() / 2;
    }

    private void drawTextureRegion(float pX, float pY) {
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
