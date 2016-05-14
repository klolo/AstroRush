package com.astro.core.engine.base;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.TextureObject;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * Created by kamil on 03.05.16.
 */
@Slf4j
public class ParalaxBackground {

    /**
     * List of the background textures.
     */
    @Getter
    private LinkedList<TextureObject> textures = new LinkedList<>();

    @GameProperty("background.amount")
    @Getter
    private int BACKGROUND_AMOUNT = 0;

    @GameProperty("background.speed")
    @Getter
    private float BACKGROUND_SPEED = 0f;

    @GameProperty("background.margin")
    @Getter
    private float TEXTURE_MARGIN_DRAW = 0f;

    @GameProperty("background.scale")
    @Getter
    private float TEXTURE_SCALE = 0f;

    @GameProperty("background.texture")
    @Getter
    private String TEXTURE_FILE = "";

    @GameProperty("background.simple")
    @Setter
    private boolean SIMPLE_MODE;

    /**
     * Density of the ground box.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    public ParalaxBackground() {
        PropertyInjector.instance.inject(this);
    }

    public void init() {
        Texture background = new Texture(Gdx.files.internal("assets/" + TEXTURE_FILE));

        if (SIMPLE_MODE) {
            textures.add(new TextureObject(new TextureRegion(background)));
            textures.get(0).getData().getSprite().setOrigin(0f, 0f);
            textures.get(0).getData().getSprite().setBounds(
                    0,
                    0,
                    Gdx.graphics.getWidth() * TEXTURE_SCALE,
                    Gdx.graphics.getHeight() * TEXTURE_SCALE);
        }
        else {
            for (int i = 0; i < BACKGROUND_AMOUNT; ++i) {
                textures.add(new TextureObject(new TextureRegion(background)));
                textures.get(i).getData().getSprite().setOrigin(0f, 0f);
                textures.get(i).getData().getSprite().setBounds(
                        (i - BACKGROUND_AMOUNT / 2) * (Gdx.graphics.getWidth() * TEXTURE_SCALE / PIXEL_PER_METER),
                        0,
                        Gdx.graphics.getWidth() * TEXTURE_SCALE,
                        Gdx.graphics.getHeight() * TEXTURE_SCALE);
            }
        }
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
     * Update for only one texture.
     */
    private void simpleUpdate(final OrthographicCamera cam, float diff) {
        textures.get(0).getData().getSprite().setBounds(
                getNewPositionX(cam, diff),
                getNewPositionY(cam, diff, textures.get(0)),
                Gdx.graphics.getWidth() * TEXTURE_SCALE,
                Gdx.graphics.getHeight() * TEXTURE_SCALE);
    }

    private boolean isOnTheLeftOfScreen(float posX, final OrthographicCamera cam) {
        float posCamX = cam.position.x / PIXEL_PER_METER;
        float worldOnScreenWidth = Gdx.graphics.getWidth() / PIXEL_PER_METER;

        if (posX < posCamX - (worldOnScreenWidth * TEXTURE_SCALE)) {
            return true;
        }

        return false;
    }


    /**
     * Find which texture is leftmost.
     *
     * @return index in textures list.
     */
    private int findFirstOnLeft() {
        int index = 0;
        float minX = textures.get(0).getData().getSprite().getX();
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
        float maxX = textures.get(0).getData().getSprite().getX();
        for (int i = 0; i < textures.size(); ++i) {
            if (textures.get(i).getData().getSprite().getX() > maxX) {
                index = i;
            }
        }

        return index;
    }

    private float getNewPositionY(OrthographicCamera cam, float diff, final TextureObject textureObject) {
        float newY;
        float worldCameraY = cam.position.y / PIXEL_PER_METER;
        float minPosY = worldCameraY - (Gdx.graphics.getHeight() * TEXTURE_MARGIN_DRAW);
        float maxPosY = worldCameraY + (Gdx.graphics.getHeight() * TEXTURE_MARGIN_DRAW);

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
    private float getNewPositionX(OrthographicCamera cam, float diff) {
        float newX;
        float worldCameraX = cam.position.x / PIXEL_PER_METER;
        float minPosX = worldCameraX - (Gdx.graphics.getWidth() * TEXTURE_MARGIN_DRAW);
        float maxPosX = worldCameraX + (Gdx.graphics.getWidth() * TEXTURE_MARGIN_DRAW);

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
