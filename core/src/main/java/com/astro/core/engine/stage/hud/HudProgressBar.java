package com.astro.core.engine.stage.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("PMD")
@Setter
@Getter
class HudProgressBar {

    private TextureRegion region;

    private float pixelPerMeter;

    private float startWidth;

    private HudElement element;

    private Location location;

    Sprite getScaledSprite(final OrthographicCamera cam, final float currentLevel, final float startLevel) {
        final float liveBarWidth = startWidth * (currentLevel / startLevel);
        final float x = getViewWith(cam) - (element.offsetX * 2);
        final Sprite s = new Sprite(region);

        region.setRegionWidth((int) liveBarWidth);

        s.setScale(1.0f, 1.0f);
        s.setOrigin(0, 0);
        s.setBounds(x + (region.getRegionWidth() / pixelPerMeter / 2), getYPosition(cam), liveBarWidth, region.getRegionHeight());

        return s;
    }

    private float getYPosition(final OrthographicCamera cam) {
        if (Location.TOP_RIGHT.equals(location) || Location.TOP_LEFT.equals(location)) {
            return getViewHeight(cam) - Math.abs(element.offsetY);
        }
        else {
            return Math.abs(element.offsetY) - getViewHeight(cam);
        }
    }

    private float getViewWith(final OrthographicCamera cam) {
        return cam.viewportWidth / 2 / pixelPerMeter;
    }

    private float getViewHeight(final OrthographicCamera cam) {
        return cam.viewportHeight / 2 / pixelPerMeter;
    }

}
