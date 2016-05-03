package com.astro.core.engine;

import com.astro.core.objects.TextureObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 */
public class Background extends TextureObject {

    private Texture background;

    private float BACKGROUND_SPEED = 1.0f;

    private float TEXTURE_MARGIN_DRAW = 0.2f;

    private float TEXTURE_SIZE_DRAW = 1.2f;

    public Background() {
        background = new Texture(Gdx.files.internal("assets/background.jpg"));
        setTextureRegion(new TextureRegion(background));

    }

    public void update(OrthographicCamera cam, float diff) {
        sprite.setBounds(
                getNewPositionX(cam,diff),
                getNewPositionY(cam,diff),
                Gdx.graphics.getWidth() * TEXTURE_SIZE_DRAW,
                Gdx.graphics.getHeight() * TEXTURE_SIZE_DRAW);
    }

    private float getNewPositionY(OrthographicCamera cam, float diff) {
        float newY;
        float worldCameraY = cam.position.y / PhysicsWorld.instance.getPIXEL_PER_METER();
        float minPosY = worldCameraY - ( Gdx.graphics.getHeight()* TEXTURE_MARGIN_DRAW);
        float maxPosY = worldCameraY + ( Gdx.graphics.getHeight() * TEXTURE_MARGIN_DRAW);

        if (sprite.getY() < minPosY) {
            newY = sprite.getX() + (diff * BACKGROUND_SPEED);
            if (newY < minPosY) {
                newY = minPosY;
            }
        }
        else if (sprite.getY() > maxPosY) {
            newY = sprite.getY() - (diff * BACKGROUND_SPEED);
            if (newY < maxPosY) {
                newY = maxPosY;
            }
        }
        else {
            newY = worldCameraY;
        }

        return newY;
    }

    private float getNewPositionX(OrthographicCamera cam, float diff) {
        float newX;
        float worldCameraX = cam.position.x / PhysicsWorld.instance.getPIXEL_PER_METER();
        float minPosX = worldCameraX - ( Gdx.graphics.getWidth()* TEXTURE_MARGIN_DRAW);
        float maxPosX = worldCameraX + ( Gdx.graphics.getWidth() * TEXTURE_MARGIN_DRAW);

        if (sprite.getX() < minPosX) {
            newX = sprite.getX() + (diff * BACKGROUND_SPEED);
            if (newX < minPosX) {
                newX = minPosX;
            }
        }
        else if (sprite.getX() > maxPosX) {
            newX = sprite.getX() - (diff * BACKGROUND_SPEED);
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
