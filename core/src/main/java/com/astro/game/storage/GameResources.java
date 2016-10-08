package com.astro.game.storage;

import com.uwsoft.editor.renderer.resources.ResourceManager;

/**
 * Singleton which protects from multiple time loading resources.
 */
public enum GameResources {
    instance;

    private static ResourceManager resourceManager;

    public synchronized ResourceManager getResourceManager() {
        if (resourceManager == null) {
            resourceManager = new ResourceManager();
            resourceManager.initAllResources();
        }

        return resourceManager;
    }
}
