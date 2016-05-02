package com.astro.core.objects;

import com.astro.core.adnotation.Dispose;
import com.astro.core.engine.PhysicsWorld;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kamil on 30.04.16.
 */
public class TextureObject extends GameObject {

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


    private void init() {
        new PropertyInjector(this);

        batch = new SpriteBatch();
        batch.enableBlending();
    }

    public TextureObject() {
        init();
    }

    /**
     * Default constructor.
     */
    public TextureObject(Texture texture) {
        init();
        this.textureRegion = new TextureRegion(texture);
        sprite = new Sprite(texture);
    }

    /**
     * Default constructor.
     */
    public TextureObject(TextureRegion textureRegion) {
        init();
        this.textureRegion = textureRegion;
        sprite = new Sprite(textureRegion);
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
        batch.setColor(sprite.getColor());

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

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
        float pX = (x + sprite.getOriginX()) * PPM - (sprite.getWidth() * sprite.getScaleX() / 2);
        float pY = (y + sprite.getOriginY()) * PPM - (sprite.getHeight() * sprite.getScaleY() / 2);

        if (rotate == 0) {
            batch.draw(textureRegion,
                    pX,
                    pY,
                    sprite.getWidth() * sprite.getScaleX(),
                    sprite.getHeight() * sprite.getScaleY()
            );
        } else {
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
            float a = (x + sprite.getOriginX()) * PPM;
            float b = (y + sprite.getOriginY()) * PPM;

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


    public void dispose() {
        batch.dispose();
    }

}
