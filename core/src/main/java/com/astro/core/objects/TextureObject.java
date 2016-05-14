package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    @Dispose
    protected TextureRegion textureRegion;

    /**
     * Batch using for rendering object.
     */
    @Dispose
    @Getter
    @Setter
    protected Batch batch;

    @Setter
    protected boolean flipX = false;

    @Setter
    protected boolean flipY = false;

    public TextureObject() {
        init();
    }

    /**
     * Default constructor.
     */
    public TextureObject(Texture texture) {
        init();
        this.textureRegion = new TextureRegion(texture);
        data.sprite = new Sprite(texture);
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
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.setColor(data.sprite.getColor());

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        render(cam, delta); // implemented in concreted class.
        batch.end();
    }

    /**
     * Called in main loop
     */
    protected void draw() {
        draw(data.sprite.getX(), data.sprite.getY(), data.sprite.getRotation());
    }

    /**
     * Render method for texture and physic object.
     */
    private void draw(float x, float y, float rotate) {
        x += data.sprite.getOriginX();
        y += data.sprite.getOriginY();

        float pX = x * PIXEL_PER_METER - data.sprite.getWidth() * data.sprite.getScaleX() / 2;
        float pY = y * PIXEL_PER_METER - data.sprite.getHeight() * data.sprite.getScaleY() / 2;

        drawTextureRegion(pX, pY, rotate);
    }

    private void drawTextureRegion(float pX, float pY, float rotate) {
        batch.draw(textureRegion,
                pX, pY, 0, 0,
                data.sprite.getWidth(),
                data.sprite.getHeight(),
                data.sprite.getScaleX(),
                data.sprite.getScaleY(),
                rotate
        );
    }
}
