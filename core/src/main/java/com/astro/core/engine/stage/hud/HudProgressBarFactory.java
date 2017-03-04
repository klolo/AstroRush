package com.astro.core.engine.stage.hud;

import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HudProgressBarFactory {

    @Autowired
    private GameResources gameResources;

    @Value("${renderer.pixel.per.meter}")
    protected short pixelPerMeter;

    HudProgressBar createFromHudElement(final HudElement element, final Location location) {
        final float startWidth = 0.7f * gameResources
                .getResourceManager()
                .getTextureRegion(element.textureName)
                .getRegionWidth();

        final HudProgressBar hudProgressBar = new HudProgressBar();
        hudProgressBar.setElement(element);
        hudProgressBar.setLocation(location);
        hudProgressBar.setPixelPerMeter(pixelPerMeter);

        final TextureRegion textureRegionCopy = new TextureRegion(gameResources
                .getResourceManager()
                .getTextureRegion(element.textureName));

        hudProgressBar.setRegion(textureRegionCopy);
        hudProgressBar.setStartWidth(startWidth);
        return hudProgressBar;
    }
}
