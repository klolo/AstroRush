package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Affine2;

import java.awt.geom.AffineTransform;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a rendereable.
 */
@Slf4j
public class GameObject implements IGameObject {

    /**
     * TODO.
     */
    @Getter
    @Setter
    protected boolean scaled = false;


    /**
     * Rendering texture.
     */
    @Dispose
    @Getter
    @Setter
    protected Texture texture;

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

    @Getter
    private Sprite sprite;

    /**
     * Default constructor.
     */
    public GameObject(Texture texture) {
        batch = new SpriteBatch();
        this.texture = texture;
        batch = new SpriteBatch();
        new PropertyInjector(this);
        sprite = new Sprite(texture);
    }

    /**
     * Default constructor.
     */
    public GameObject(TextureRegion textureRegion) {
        batch = new SpriteBatch();
        batch.enableBlending();
        this.textureRegion = textureRegion;
        batch = new SpriteBatch();
        sprite = new Sprite(textureRegion);
    }

    public void dispose() {
        batch.dispose();
    }

    public void render(OrthographicCamera cam) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        render();
        batch.end();
    }

    public void render() {
        float PPM = PhysicsWorld.instance.getPIXEL_PER_METER();
        float x = sprite.getX() - (sprite.getWidth() / PPM / 2);
        float y = sprite.getY() - (sprite.getHeight() / PPM / 2);

            if (sprite.getRotation() != 0.0f) {
                sprite.setOriginCenter();
                sprite.draw(batch);
            }
            else {
                draw(sprite.getX(), sprite.getY());
            }
    }

    public void draw(float x, float y) {
        float PPM = PhysicsWorld.instance.getPIXEL_PER_METER();
        if (textureRegion != null) {
            batch.draw(textureRegion,
                    x * PPM - (sprite.getWidth() * sprite.getScaleX() / 2),
                    y * PPM - (sprite.getHeight() * sprite.getScaleY() / 2),
                    sprite.getWidth() * sprite.getScaleX(),
                    sprite.getHeight() * sprite.getScaleY()
            );
        }
        else {
            batch.draw(texture,
                    x * PPM - (sprite.getWidth() * sprite.getScaleX() / 2),
                    y * PPM - (sprite.getHeight() * sprite.getScaleY() / 2),
                    sprite.getWidth(), sprite.getHeight());
        }
    }
}
