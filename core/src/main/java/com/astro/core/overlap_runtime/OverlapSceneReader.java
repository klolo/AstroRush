package com.astro.core.overlap_runtime;

import com.astro.core.engine.stage.GameObjectUtil;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.loaders.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.common.base.Preconditions;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Overlap 2d map loader.
 */
@Slf4j
public class OverlapSceneReader {

    @Autowired
    ComponentLoader componentLoader;

    @Autowired
    ParticleEffectsLoader particleEffectsLoader;

    @Autowired
    LightsLoader lightsLoader;

    @Autowired
    LabelsLoader labelsLoader;

    @Autowired
    SpriteAnimationsLoader spriteAnimationsLoader;

    @Getter
    private List<IGameObject> components = new ArrayList<>();

    private final static String UNKNOWN_COMPONENT_MSG = "Unknown component error";

    /**
     * Opens and read json map file.
     */
    public List<IGameObject> loadScene(final String scenePath) {
        Preconditions.checkNotNull(scenePath, "ScenePath cannot be null");

        components = new ArrayList<>();
        final FileHandle file = Gdx.files.internal(scenePath);
        final CompositeVO rootComposite = new Json().fromJson(SceneVO.class, file.readString()).composite;

        loadComposite(rootComposite);
        return components;
    }

    /**
     * Converting all elements in json map file.
     */
    private OverlapSceneReader loadComposite(final CompositeVO rootComposite) {
        if (rootComposite.layers != null) {
            rootComposite.layers.stream().forEach(layer -> GameObjectUtil.instance.addLayer(layer.layerName));
        }

        return processComponentList(rootComposite.sImages, componentLoader)
                .processComponentList(rootComposite.sParticleEffects, particleEffectsLoader)
                .processComponentList(rootComposite.sLights, lightsLoader)
                .processComponentList(rootComposite.sLabels, labelsLoader)
                .processComponentList(rootComposite.sSpriteAnimations, spriteAnimationsLoader)
                .unsupportedComponent(rootComposite.sImage9patchs)
                .unsupportedComponent(rootComposite.sTextBox)
                .unsupportedComponent(rootComposite.sSelectBoxes)
                .unsupportedComponent(rootComposite.sSpineAnimations)
                .unsupportedComponent(rootComposite.sSpriterAnimations)
                .loadOtherComposites(rootComposite);
    }

    /**
     * Convert readed from map file data to IGameObject and storage it in compononts.
     */
    <T> OverlapSceneReader processComponentList(final ArrayList<T> componentsToLoad, final ILoader<T> loader) {
        Preconditions.checkArgument(componentsToLoad != null, "Object for register should not be null");
        componentsToLoad
                .stream()
                .map(e -> register(loader, e))
                .filter(e -> e != null)
                .forEach(e -> components.add(e));
        return this;
    }

    /**
     * Generic method for reading different kind of elements.
     */
    private <T> IGameObject register(final ILoader<T> loader, T element) {
        Preconditions.checkNotNull(element, "Element to register cannot be null");
        return loader.register(element);
    }


    /**
     * Register SpriterVO.
     */
    OverlapSceneReader unsupportedComponent(final ArrayList components) {
        if (components != null && components.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Loading composistes.
     */
    OverlapSceneReader loadOtherComposites(final CompositeVO otherComposites) {
        Preconditions.checkArgument(otherComposites != null, "Object rootComposite for register should not be null");
        otherComposites.sComposites.forEach(e -> this.loadComposite(e.composite, e.originX, e.originY, e.x, e.y, e.rotation));
        return this;
    }

    /**
     * Converting all elements in json map file.
     */
    private OverlapSceneReader loadComposite(final CompositeVO rootComposite,
                                             final float originX, final float originY,
                                             final float parentX, final float parentY, final float rotate) {
        rootComposite.sImages.forEach(e -> {
            e.x += parentX;
            e.y += parentY;
            e.originX += originX;
            e.originY += originY;
            e.rotation += rotate;
        });

        return loadComposite(rootComposite);
    }
}
