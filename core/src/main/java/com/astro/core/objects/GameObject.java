package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.storage.PropertyInjector;
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
 * Represents a rendereable.
 */
@Slf4j
abstract public class GameObject implements IGameObject {

    /**
     * Name of the object.
     */
    @Getter
    @Setter
    protected String name = "";


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
    protected Sprite sprite;

    /**
     * Default constructor.
     */
    public GameObject(Texture texture) {
        init();
        this.textureRegion = new TextureRegion(texture);
        sprite = new Sprite(texture);
    }

    /**
     * Default constructor.
     */
    public GameObject() {
        init();
        sprite = new Sprite();
    }

    /**
     * Default constructor.
     */
    public GameObject(TextureRegion textureRegion) {
        init();
        this.textureRegion = textureRegion;
        sprite = new Sprite(textureRegion);
    }

    private void init() {
        new PropertyInjector(this);

        batch = new SpriteBatch();
        batch.enableBlending();
    }

    public void dispose() {
        batch.dispose();
    }

    protected abstract void render(OrthographicCamera cam, float delta);

    /**
     * Every object should be rendered by this method.
     */
    public void show(OrthographicCamera cam, float delta) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
;
        batch.setColor(sprite.getColor());

        render(cam, delta); // implemented in concreted class.

        batch.end();
    }


    /**
     * Called in main loop
     */
    protected void draw() {
        draw(sprite.getX(), sprite.getY(), sprite.getRotation());
    }

    /**
     * Render method for texture and physic object.
     */
    private void draw(float x, float y, float rotate) {
        float PPM = PhysicsWorld.instance.getPIXEL_PER_METER();
        float pX = x * PPM - (sprite.getWidth() * sprite.getScaleX() / 2);
        float pY = y * PPM - (sprite.getHeight() * sprite.getScaleY() / 2);

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
}
