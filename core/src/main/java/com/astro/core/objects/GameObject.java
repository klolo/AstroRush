package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a rendereable.
 */
@Slf4j
public class GameObject implements IGameObject {

    /**
     * Name of the object.
     */
    @Getter
    @Setter
    protected String name = "";


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
    public GameObject() {
        batch = new SpriteBatch();
        batch = new SpriteBatch();
        new PropertyInjector(this);
        sprite = new Sprite();
    }

    /**
     * Default constructor.
     */
    public GameObject(TextureRegion textureRegion) {
        batch = new SpriteBatch();
        batch.enableBlending();
        this.textureRegion = textureRegion;
        batch = new PolygonSpriteBatch();
        sprite = new Sprite(textureRegion);
    }

    public void dispose() {
        batch.dispose();
    }

    /**
     * Called in main loop
     */
    public void render(OrthographicCamera cam, float delta) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.enableBlending();

        draw(sprite.getX(), sprite.getY(), sprite.getRotation(), cam);

        batch.end();
    }

    /**
     * Render method for texture and physic object.
     */
    private void draw(float x, float y, float rotate, OrthographicCamera cam) {
        float PPM = PhysicsWorld.instance.getPIXEL_PER_METER();
        float pX = x * PPM - (sprite.getWidth() * sprite.getScaleX() / 2);
        float pY = y * PPM - (sprite.getHeight() * sprite.getScaleY() / 2);

        if (textureRegion != null) {
            if (rotate == 0) {
                batch.draw(textureRegion,
                        pX,
                        pY,
                        sprite.getWidth() * sprite.getScaleX(),
                        sprite.getHeight() * sprite.getScaleY()
                );
            }
            else {
                /**
                 * Podczas rotacji obiekty box2d nie pokrywaja sie z tekxturami,
                 * poniewaz box2d wykonuje obrot dookola srodka ciezkosci a libgdx dookola
                 * lewego dolnego wierzcholka. Dlatego lewy dolny wierzcholek nalezy najpierw obrocic wzgledem
                 * srodka ciezkosci o podany kat, a nastepnie w wyliczonym punkcie ustawiamy figure i wykonujemy
                 * obrot.
                 *
                 * <a href="http://www.megamatma.pl/uczniowie/wzory/geometria-analityczna/przeksztalcenia-na-plaszczyznie">
                 *  Obrót o dany kąt dowolnego punktu
                 * </a>
                 *
                 */
                float a = x * PPM;
                float b = y * PPM;

                float angle = (float) Math.toRadians(rotate);

                float sin = (float) Math.sin(angle);
                float cos = (float) Math.cos(angle);

                float x1 = a + (pX - a) * cos - (pY - b) * sin;
                float y1 = b + (pX - a) * sin + (pY - b) * cos;

                batch.draw(textureRegion,
                        x1, y1, 0, 0,
                        sprite.getWidth(),
                        sprite.getHeight(),
                        sprite.getScaleX(),
                        sprite.getScaleY(),
                        rotate
                );
            }
        }
        else {
            batch.draw(texture,
                    x * PPM - (sprite.getWidth() * sprite.getScaleX() / 2),
                    y * PPM - (sprite.getHeight() * sprite.getScaleY() / 2),
                    sprite.getWidth(), sprite.getHeight());
        }
    }
}
