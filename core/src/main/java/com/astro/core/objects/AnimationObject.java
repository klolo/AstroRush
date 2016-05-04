package com.astro.core.objects;

import com.astro.core.engine.PhysicsWorld;
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
        TextureRegion textureRegion = animation.getKeyFrame(elapsedTime, true);

        float width = textureRegion.getRegionWidth();
        float height = textureRegion.getRegionHeight();

        float x = sprite.getX() * PhysicsWorld.instance.PIXEL_PER_METER;
        float y = sprite.getY() * PhysicsWorld.instance.PIXEL_PER_METER;

        if (flipX) {
            width = width * -1;
            x += textureRegion.getRegionWidth();
        }

        if (flipY) {
            height = height * -1;
        }

        batch.draw(textureRegion, x, y, width, height);
    }
}
