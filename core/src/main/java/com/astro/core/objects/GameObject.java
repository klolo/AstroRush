package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.astro.core.engine.PhysicsWorld;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a rendereable.
 */
@Slf4j
public class GameObject implements IGameObject {

    /**
     * Width of the object on the screen.
     */
    @Getter
    @Setter
    protected float width;

    /**
     * Height of the object on the screen.
     */
    @Getter
    @Setter
    protected float height;

    /**
     * Position X of the object.
     */
    @Getter
    @Setter
    protected float posX = 100;

    /**
     * Positions Y of the object.
     */
    @Getter
    @Setter
    protected float posY = 100;

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

    protected PolygonRegion polygonRegion;

    /**
     * Default constructor.
     */
    public GameObject(Texture texture) {
        batch = new SpriteBatch();
        this.texture = texture;
        batch = new SpriteBatch();
    }

    /**
     * Default constructor.
     */
    public GameObject(TextureRegion textureRegion) {
        batch = new SpriteBatch();
        this.textureRegion = textureRegion;
        batch = new SpriteBatch();
    }


    /**
     * Default constructor.
     */
    public GameObject(PolygonRegion polygonRegion) {
        batch = new SpriteBatch();
        this.polygonRegion = polygonRegion;
        batch = new PolygonSpriteBatch();
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
        float x = posX * PPM - (width / 2);
        float y = posY * PPM - (height / 2);

        if (polygonRegion != null) {
            ((PolygonSpriteBatch) batch).draw(polygonRegion, x, y);
        }
        else if (textureRegion == null) {
            batch.draw(texture,
                    x,
                    y,
                    width,
                    height
            );
        }
        else {
            batch.draw(textureRegion,
                    (posX * PPM) + (textureRegion.getRegionWidth()/PPM),
                    (posY * PPM) + (textureRegion.getRegionHeight()/PPM),
                    width,
                    height
            );
        }
    }
}
