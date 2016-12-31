package com.astro.core.engine.stage.hud;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HudProgressBar {

    private final TextureRegion region;

    private final float pixelPerMeter;

    public HudProgressBar(final TextureRegion region, final float pixelPerMeter) {
        this.region = region;
        this.pixelPerMeter = pixelPerMeter;
    }

    public Sprite getScaledSprite(final OrthographicCamera cam, final float currentLevel, final float startLevel, final float startWidth) {
        final float liveBarWidth = startWidth * currentLevel / startLevel;
        region.setRegionWidth((int) liveBarWidth);

        float x = getViewWith(cam) - 2.6f;
        final float y = getViewHeight(cam) - 0.51f;

        x += liveBarWidth / pixelPerMeter / 2;

        final Sprite s = new Sprite(region);

        s.setScale(1.0f, 1.0f);
        s.setOrigin(0, 0);
        s.setBounds(x, y, liveBarWidth, region.getRegionHeight() + 4);

        return s;
    }

    private float getViewWith(final OrthographicCamera cam) {
        return cam.viewportWidth / 2 / pixelPerMeter;
    }

    private float getViewHeight(final OrthographicCamera cam) {
        return cam.viewportHeight / 2 / pixelPerMeter;
    }

}
