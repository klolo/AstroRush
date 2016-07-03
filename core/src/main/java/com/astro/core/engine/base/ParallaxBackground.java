package com.astro.core.engine.base;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.objects.TextureObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * Movable background.
 */
@Slf4j
public class ParallaxBackground {

    /**
     * List of the background textures.
     */
    @Getter
    private LinkedList<TextureObject> textures = new LinkedList<>();

    @GameProperty("background.amount")
    @Getter
    int BACKGROUND_AMOUNT = 0;

    @GameProperty("background.speed")
    @Getter
    private float BACKGROUND_SPEED = 0f;

    @GameProperty("background.margin")
    @Getter
    private float TEXTURE_MARGIN_DRAW;

    @GameProperty("background.scale")
    @Getter
    private float TEXTURE_SCALE;

    @GameProperty("background.texture")
    @Getter
    private String TEXTURE_FILE = "";

    @GameProperty("background.simple")
    @Setter
    boolean SIMPLE_MODE;

    /**
     * Density of the ground box.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER;

    private float lastWidth;

    private float lastHeight;

    public ParallaxBackground() {
        PropertyInjector.instance.inject(this);
    }

    public void init() {
        Texture background = new Texture(Gdx.files.internal("assets/" + TEXTURE_FILE));

        if (SIMPLE_MODE) {
            textures.add(new TextureObject(new TextureRegion(background)));
            textures.get(0).getData().getSprite().setOrigin(0f, 0f);
        }
        else {
            for (int i = 0; i < BACKGROUND_AMOUNT; ++i) {
                textures.add(new TextureObject(new TextureRegion(background)));
                textures.get(i).getData().getSprite().setOrigin(0f, 0f);
            }
        }

        resizeImages();
    }

    /**
     * Drawing all textures for background.
     */
    public void show(OrthographicCamera cam, float diff) {
        if (SIMPLE_MODE) {
            textures.get(0).show(cam, diff);
        }
        else {
            textures.forEach(t -> t.show(cam, diff));
        }
    }


    public void update(OrthographicCamera cam, float diff) {
        if (SIMPLE_MODE) {
            simpleUpdate(cam, diff);
        }
        else {
            textures.forEach(t -> t.getData().getSprite().setY(getNewPositionY(cam, diff, t)));
            textures.forEach(t -> t.getData().getSprite().setX(t.getData().getSprite().getX() - (diff * BACKGROUND_SPEED)));

            int findFirstOnLeftIndex = findFirstOnLeft();
            float firstPosX = textures.get(findFirstOnLeftIndex).getData().getSprite().getX();

            if (isOnTheLeftOfScreen(firstPosX, cam)) {
                log.info("Switch texture {} to end", textures.get(findFirstOnLeftIndex));
                float lastPosX = textures.get(findLastOnLeft()).getData().getSprite().getX();
                textures.get(findFirstOnLeftIndex).getData().getSprite().setX(
                        lastPosX + (Gdx.graphics.getWidth() * TEXTURE_SCALE / PIXEL_PER_METER)
                );
            }
        }
    }

    /**
     * Resaizing background.
     */
    public void resize(int w, int h) {
        if (w != lastWidth || h != lastHeight) {
            resizeImages();
            lastWidth = w;
            lastHeight = h;
        }
    }

    /**
     * Changing size of the TextureRegion.
     */

    private void resizeImages() {
        if (SIMPLE_MODE) {
            textures.get(0).getData().getSprite().setBounds(
                    0,
                    0,
                    Gdx.graphics.getWidth() * TEXTURE_SCALE,
                    Gdx.graphics.getHeight() * TEXTURE_SCALE);
        }
        else {
            for (int i = 0; i < BACKGROUND_AMOUNT; ++i) {
                textures.get(i).getData().getSprite().setBounds(
                        (i - BACKGROUND_AMOUNT / 2) * (Gdx.graphics.getWidth() * TEXTURE_SCALE / PIXEL_PER_METER),
                        0,
                        Gdx.graphics.getWidth() * TEXTURE_SCALE,
                        Gdx.graphics.getHeight() * TEXTURE_SCALE);
            }
        }
    }

    /**
     * Update for only one texture.
     */
    private void simpleUpdate(final OrthographicCamera cam, final float diff) {
        textures.get(0).getData().getSprite().setBounds(
                getNewPositionX(cam, diff),
                getNewPositionY(cam, diff, textures.get(0)),
                Gdx.graphics.getWidth() * TEXTURE_SCALE,
                Gdx.graphics.getHeight() * TEXTURE_SCALE);
    }

    private boolean isOnTheLeftOfScreen(final float posX, final OrthographicCamera cam) {
        final float posCamX = cam.position.x / PIXEL_PER_METER;
        final float worldOnScreenWidth = (Gdx.graphics.getWidth() / PIXEL_PER_METER) * TEXTURE_SCALE;
        return posX < posCamX - worldOnScreenWidth;
    }

    /**
     * Find which texture is leftmost.
     *
     * @return index in textures list.
     */
    private int findFirstOnLeft() {
        int index = 0;
        final float minX = textures.get(0).getData().getSprite().getX();
        for (int i = 0; i < textures.size(); ++i) {
            if (textures.get(i).getData().getSprite().getX() < minX) {
                index = i;
            }
        }

        return index;
    }

    /**
     * Find which texture is leftmost.
     *
     * @return index in textures list.
     */
    private int findLastOnLeft() {
        int index = 0;
        final float maxX = textures.get(0).getData().getSprite().getX();
        for (int i = 0; i < textures.size(); ++i) {
            if (textures.get(i).getData().getSprite().getX() > maxX) {
                index = i;
            }
        }

        return index;
    }

    private float getNewPositionY(final OrthographicCamera cam, float diff, final TextureObject textureObject) {
        float newY;
        final float worldCameraY = cam.position.y / PIXEL_PER_METER;
        final float minPosY = worldCameraY - (Gdx.graphics.getHeight() * TEXTURE_MARGIN_DRAW);
        final float maxPosY = worldCameraY + (Gdx.graphics.getHeight() * TEXTURE_MARGIN_DRAW);

        if (textureObject.getData().getSprite().getY() < minPosY) {
            newY = textureObject.getData().getSprite().getX() + (diff * BACKGROUND_SPEED);
            if (newY < minPosY) {
                newY = minPosY;
            }
        }
        else if (textureObject.getData().getSprite().getY() > maxPosY) {
            newY = textureObject.getData().getSprite().getY() - (diff * BACKGROUND_SPEED);
            if (newY < maxPosY) {
                newY = maxPosY;
            }
        }
        else {
            newY = worldCameraY;
        }

        return newY;
    }

    /**
     * For simple mode only.
     */
    private float getNewPositionX(final OrthographicCamera cam, float diff) {
        float newX;
        final float worldCameraX = cam.position.x / PIXEL_PER_METER;
        final float minPosX = worldCameraX - (Gdx.graphics.getWidth() * TEXTURE_MARGIN_DRAW);
        final float maxPosX = worldCameraX + (Gdx.graphics.getWidth() * TEXTURE_MARGIN_DRAW);

        if (textures.get(0).getData().getSprite().getX() < minPosX) {
            newX = textures.get(0).getData().getSprite().getX() + (diff * BACKGROUND_SPEED);
            if (newX < minPosX) {
                newX = minPosX;
            }
        }
        else if (textures.get(0).getData().getSprite().getX() > maxPosX) {
            newX = textures.get(0).getData().getSprite().getX() - (diff * BACKGROUND_SPEED);
            if (newX < maxPosX) {
                newX = maxPosX;
            }
        }
        else {
            newX = worldCameraX;
        }

        return newX;
    }

}