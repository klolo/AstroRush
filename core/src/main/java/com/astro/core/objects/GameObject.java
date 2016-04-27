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

    @GameProperty("renderer.pixel.per.meter")
    @Getter
    private int PIXEL_PER_METER = 0;


    protected PolygonRegion polygonRegion;

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

        if (polygonRegion != null) {
            ((PolygonSpriteBatch) batch).draw(polygonRegion, x, y);
        }
        else if (textureRegion == null) {
            batch.draw(texture,
                    x,
                    y,
                    sprite.getWidth(),
                    sprite.getHeight()
            );
        }
        else {


            if (sprite.getRotation() != 0.0f) {
//                sprite.setPosition(
//                        sprite.getX() * PPM - ((sprite.getWidth() * sprite.getScaleX() * (float) Math.cos(sprite.getRotation())) / 2),
//                        sprite.getY() * PPM - ((sprite.getHeight() * sprite.getScaleY()) * (float) Math.sin(sprite.getRotation())) / 2);

                float oldX = sprite.getX();
                float oldY = sprite.getY();

                sprite.setPosition(
                        oldX + ((sprite.getWidth()*sprite.getScaleX())),
                        oldY + (sprite.getHeight()*sprite.getScaleY()));
                sprite.draw(batch);
                sprite.setPosition(oldX, oldY);

//                batch.draw(textureRegion,
//                        posX * PPM - ((width) * (float) Math.cos(rotation) * scaleX  ),
//                        posY * PPM - ((width) * (float) Math.sin(rotation)) /2 ,
//                        textureRegion.getRegionX(),
//                        textureRegion.getRegionY(),
//                        width,
//                        height,
//                        scaleX,
//                        scaleY,
//                        rotation
//                );
            }
            else {
                batch.draw(textureRegion,
                        sprite.getX() * PPM - (sprite.getWidth() * sprite.getScaleX() / 2),
                        sprite.getY() * PPM - (sprite.getHeight() * sprite.getScaleY() / 2),
                        sprite.getWidth() * sprite.getScaleX(),
                        sprite.getHeight() * sprite.getScaleY()
                );
            }


        }
    }
}
