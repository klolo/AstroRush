package com.astro.core;

import com.uwsoft.editor.renderer.resources.ResourceManager;

/**
 * Created by kamil on 11.05.16.
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
