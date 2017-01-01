package com.astro.core.engine.stage.hud;

import com.astro.core.storage.GameResources;
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
        final float startWidth = gameResources
                .getResourceManager()
                .getTextureRegion(element.textureName)
                .getRegionWidth();

        final HudProgressBar hudProgressBar = new HudProgressBar();
        hudProgressBar.setElement(element);
        hudProgressBar.setLocation(location);
        hudProgressBar.setPixelPerMeter(pixelPerMeter);
        hudProgressBar.setRegion(gameResources
                .getResourceManager()
                .getTextureRegion(element.textureName));
        hudProgressBar.setStartWidth(startWidth);
        return hudProgressBar;
    }
}
