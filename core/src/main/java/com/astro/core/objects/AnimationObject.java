package com.astro.core.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by kamil on 01.05.16.
 */
public class AnimationObject extends TextureObject {

    @Getter
    @Setter
    private Animation animation;

    private float elapsedTime = 0;

    public AnimationObject(int fps, final TextureAtlas atlas) {
        animation = new Animation(1 / fps, atlas.getRegions());
    }

    @Override
    protected void render(OrthographicCamera cam, float delta) {
        elapsedTime += delta;
        textureRegion = animation.getKeyFrame(elapsedTime, true);

        float width = textureRegion.getRegionWidth();
        float height = textureRegion.getRegionHeight();

        float x = data.sprite.getX() + data.sprite.getOriginX();
        float y = data.sprite.getY() + data.sprite.getOriginY();

        float pX = getPx(x, width);
        float pY = getPy(y, height);

        if (flipX) {
            width *= -1;
            pX -= width * data.sprite.getScaleX();
        }

        if (flipY) {
            height = height * -1;
        }

        batch.draw(textureRegion, pX, pY, width * data.sprite.getScaleX(), height * data.sprite.getScaleY());
    }

}
