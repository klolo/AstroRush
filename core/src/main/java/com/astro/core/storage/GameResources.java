package com.astro.core.storage;

import com.uwsoft.editor.renderer.resources.ResourceManager;
import org.springframework.stereotype.Component;

/**
 * Singleton which protects from multiple time loading resources.
 */
@Component
public class GameResources {

    private static ResourceManager resourceManager;

    public synchronized ResourceManager getResourceManager() {
        if (resourceManager == null) {
            resourceManager = new ResourceManager();
            resourceManager.initAllResources();
        }

        return resourceManager;
    }
}
