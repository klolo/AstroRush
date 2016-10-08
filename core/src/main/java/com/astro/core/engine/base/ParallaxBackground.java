package com.astro.core.engine.base;

import com.astro.core.objects.TextureObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * Movable background.
 */
@Slf4j
@Component
@Scope("prototype")
public class ParallaxBackground implements ApplicationContextAware {

    /**
     * List of the background textures.
     */
    @Getter
    private LinkedList<TextureObject> textures = new LinkedList<>();

    @Value("${background.amount}")
    @Getter
    int backgroundAmount;

    @Value("${background.speed}")
    @Getter
    private float backgroundSpeed;

    @Value("${background.margin}")
    @Getter
    private float textureMarginDraw;

    @Value("${background.scale}")
    @Getter
    private float textureScale;

    @Value("${background.texture}")
    @Getter
    private String textureFile = "";

    @Value("${background.simple}")
    @Setter
    boolean simpleMode;

    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter;

    private float lastWidth;

    private float lastHeight;

    @Setter
    private ApplicationContext applicationContext;

    public void init() {
        final Texture background = new Texture(Gdx.files.internal("assets/" + textureFile));

        if (simpleMode) {
            final TextureObject textureObject =
                    applicationContext.getBean("textureObject", TextureObject.class);
            textureObject.setTextureRegion(new TextureRegion(background));

            textures.add(textureObject);
            textures.get(0).getData().getSprite().setOrigin(0f, 0f);
        }
        else {
            for (int i = 0; i < backgroundAmount; ++i) {
                textures.add(createTexture(background));
                textures.get(i).getData().getSprite().setOrigin(0f, 0f);
            }
        }

        resizeImages();
    }

    private TextureObject createTexture(final Texture background) {
        final TextureObject textureObject =
                applicationContext.getBean("textureObject", TextureObject.class);
        textureObject.setTextureRegion(new TextureRegion(background));
        return textureObject;
    }

    /**
     * Drawing all textures for background.
     */
    public void show(final OrthographicCamera cam, final float diff) {
        if (simpleMode) {
            textures.get(0).show(cam, diff);
        }
        else {
            textures.forEach(t -> t.show(cam, diff));
        }
    }


    public void update(final OrthographicCamera cam, final float diff) {
        if (simpleMode) {
            simpleUpdate(cam, diff);
        }
        else {
            textures.forEach(t -> t.getData().getSprite().setY(getNewPositionY(cam, diff, t)));
            textures.forEach(t -> t.getData().getSprite().setX(t.getData().getSprite().getX() - (diff * backgroundSpeed)));

            final int findFirstOnLeftIndex = findFirstOnLeft();
            final float firstPosX = textures.get(findFirstOnLeftIndex).getData().getSprite().getX();

            if (isOnTheLeftOfScreen(firstPosX, cam)) {
                LOGGER.info("Switch texture {} to end", textures.get(findFirstOnLeftIndex));
                final float lastPosX = textures.get(findLastOnLeft()).getData().getSprite().getX();
                textures.get(findFirstOnLeftIndex).getData().getSprite().setX(
                        lastPosX + (Gdx.graphics.getWidth() * textureScale / pixelPerMeter)
                );
            }
        }
    }

    /**
     * Resaizing background.
     */
    public void resize(final int w, final int h) {
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
        if (simpleMode) {
            textures.get(0).getData().getSprite().setBounds(
                    0,
                    0,
                    Gdx.graphics.getWidth() * textureScale,
                    Gdx.graphics.getHeight() * textureScale);
        }
        else {
            for (int i = 0; i < backgroundAmount; ++i) {
                textures.get(i).getData().getSprite().setBounds(
                        (i - backgroundAmount / 2) * (Gdx.graphics.getWidth() * textureScale / pixelPerMeter),
                        0,
                        Gdx.graphics.getWidth() * textureScale,
                        Gdx.graphics.getHeight() * textureScale);
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
                Gdx.graphics.getWidth() * textureScale,
                Gdx.graphics.getHeight() * textureScale);
    }

    private boolean isOnTheLeftOfScreen(final float posX, final OrthographicCamera cam) {
        final float posCamX = cam.position.x / pixelPerMeter;
        final float worldOnScreenWidth = (Gdx.graphics.getWidth() / pixelPerMeter) * textureScale;
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

    private float getNewPositionY(final OrthographicCamera cam, final float diff, final TextureObject textureObject) {
        float newY;
        final float worldCameraY = cam.position.y / pixelPerMeter;
        final float minPosY = worldCameraY - (Gdx.graphics.getHeight() * textureMarginDraw);
        final float maxPosY = worldCameraY + (Gdx.graphics.getHeight() * textureMarginDraw);

        if (textureObject.getData().getSprite().getY() < minPosY) {
            newY = textureObject.getData().getSprite().getX() + (diff * backgroundSpeed);
            if (newY < minPosY) {
                newY = minPosY;
            }
        }
        else if (textureObject.getData().getSprite().getY() > maxPosY) {
            newY = textureObject.getData().getSprite().getY() - (diff * backgroundSpeed);
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
    private float getNewPositionX(final OrthographicCamera cam, final float diff) {
        float newX;
        final float worldCameraX = cam.position.x / pixelPerMeter;
        final float minPosX = worldCameraX - (Gdx.graphics.getWidth() * textureMarginDraw);
        final float maxPosX = worldCameraX + (Gdx.graphics.getWidth() * textureMarginDraw);

        if (textures.get(0).getData().getSprite().getX() < minPosX) {
            newX = textures.get(0).getData().getSprite().getX() + (diff * backgroundSpeed);
            if (newX < minPosX) {
                newX = minPosX;
            }
        }
        else if (textures.get(0).getData().getSprite().getX() > maxPosX) {
            newX = textures.get(0).getData().getSprite().getX() - (diff * backgroundSpeed);
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
