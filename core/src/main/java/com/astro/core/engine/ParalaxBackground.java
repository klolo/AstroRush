package com.astro.core.engine;

import com.astro.core.objects.TextureObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 03.05.16.
 */
@Slf4j
public class ParalaxBackground {

    private Texture background;

    private TextureObject textureObjectA;

    private TextureObject textureObjectB;

    private float BACKGROUND_SPEED = 4.0f;

    private float TEXTURE_MARGIN_DRAW = 0.2f;

    private float TEXTURE_SIZE_DRAW = 1.2f;

    float SCROL_SPEED = 1f;

    public ParalaxBackground() {
        background = new Texture(Gdx.files.internal("assets/background.jpg"));

        textureObjectA = new TextureObject(new TextureRegion(background));
        textureObjectB = new TextureObject(new TextureRegion(background));
        textureObjectA.getSprite().setOrigin(0f, 0f);
        textureObjectA.getSprite().setBounds(
                0,
                0,
                (Gdx.graphics.getWidth() * TEXTURE_SIZE_DRAW),
                (Gdx.graphics.getHeight() * TEXTURE_SIZE_DRAW));

        textureObjectB.getSprite().setOrigin(0f, 0f);
        textureObjectB.getSprite().setBounds(
                (Gdx.graphics.getWidth() / 80),
                0,
                (Gdx.graphics.getWidth() * TEXTURE_SIZE_DRAW),
                (Gdx.graphics.getHeight() * TEXTURE_SIZE_DRAW));
    }

    public void show(OrthographicCamera cam, float diff) {
        textureObjectA.show(cam, diff);
        textureObjectB.show(cam, diff);
    }

    private float getNewPositionY(OrthographicCamera cam, float diff, final TextureObject textureObject) {
        float newY;
        float worldCameraY = cam.position.y / PhysicsWorld.instance.getPIXEL_PER_METER();
        float minPosY = worldCameraY - (Gdx.graphics.getHeight() * TEXTURE_MARGIN_DRAW);
        float maxPosY = worldCameraY + (Gdx.graphics.getHeight() * TEXTURE_MARGIN_DRAW);

        if (textureObject.getSprite().getY() < minPosY) {
            newY = textureObject.getSprite().getX() + (diff * BACKGROUND_SPEED);
            if (newY < minPosY) {
                newY = minPosY;
            }
        }
        else if (textureObject.getSprite().getY() > maxPosY) {
            newY = textureObject.getSprite().getY() - (diff * BACKGROUND_SPEED);
            if (newY < maxPosY) {
                newY = maxPosY;
            }
        }
        else {
            newY = worldCameraY;
        }

        return newY;
    }

    public void update(OrthographicCamera cam, float diff) {
        float posCamX = cam.position.x / 80;

        float worldOnScreenWidth = (Gdx.graphics.getWidth() / 80);
        float switchPoint = (float) (worldOnScreenWidth * 1.2);

        if (textureObjectA.getSprite().getX() < textureObjectB.getSprite().getX()) {
            if ((textureObjectA.getSprite().getX() < posCamX - (worldOnScreenWidth * 1.5))) {
                log.info("switch A behind B");
                textureObjectA.getSprite().setX(
                        textureObjectB.getSprite().getX() + worldOnScreenWidth * TEXTURE_SIZE_DRAW
                );
            }
        }
        else {
            if (textureObjectB.getSprite().getX() < posCamX - (worldOnScreenWidth * 1.5)) {
                log.info("switch B behind A");
                textureObjectB.getSprite().setX(
                        textureObjectA.getSprite().getX() + worldOnScreenWidth * TEXTURE_SIZE_DRAW
                );
            }
        }

        textureObjectA.getSprite().setX(textureObjectA.getSprite().getX() - diff * SCROL_SPEED);
        textureObjectA.getSprite().setY(getNewPositionY(cam, diff, textureObjectA));

        textureObjectB.getSprite().setX(textureObjectB.getSprite().getX() - diff * SCROL_SPEED);
        textureObjectB.getSprite().setY(getNewPositionY(cam, diff, textureObjectB));
    }

}
