package com.astro.core.objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import lombok.Getter;
import lombok.Setter;


public class AnimationObject extends TextureObject {

    @Getter
    @Setter
    private Animation animation;

    private float elapsedTime = 0;

    public void initAnimation(final int fps, final TextureAtlas atlas) {
        animation = new Animation(1 / fps, atlas.getRegions());
    }

    @Override
    public void render(final OrthographicCamera cam, final float delta) {
        elapsedTime += delta;
        textureRegion = animation.getKeyFrame(elapsedTime, true);

        float width = textureRegion.getRegionWidth();
        float height = textureRegion.getRegionHeight();

        final float x = data.sprite.getX() + data.sprite.getOriginX();
        final float y = data.sprite.getY() + data.sprite.getOriginY();

        float pX = getPx(x, width);
        final float pY = getPy(y, height);

        if (data.flipX) {
            width *= -1;
            pX -= width * data.sprite.getScaleX();
        }

        if (data.flipY) {
            height = height * -1;
        }

        batch.draw(textureRegion, pX, pY, width * data.sprite.getScaleX(), height * data.sprite.getScaleY());
    }

}
