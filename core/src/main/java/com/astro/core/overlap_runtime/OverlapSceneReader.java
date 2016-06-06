package com.astro.core.overlap_runtime;

import com.astro.core.engine.stage.GameObjectUtil;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.loaders.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public class OverlapSceneReader {

    @Getter
    private String scenePath = "";

    @Getter
    List<IGameObject> components = new ArrayList<>();

    /**
     * Requires pass path to the map file.
     *
     * @param scenePath path to file contains map
     */
    public OverlapSceneReader(final String scenePath) {
        this.scenePath = scenePath;
    }

    final static String UNKNOWN_COMPONENT_MSG = "Unknown component error";

    /**
     * Opens and read json map file.
     */
    public OverlapSceneReader loadScene() {
        FileHandle file = Gdx.files.internal(scenePath);

        CompositeVO rootComposite = new Json().fromJson(SceneVO.class, file.readString()).composite;
        loadComposite(rootComposite);
        return this;
    }

    /**
     * Generic method for reading different kind of elements.
     */
    private <T> IGameObject register(final ILoader<T> loader, T element) {
        return loader.register(element);
    }

    /**
     * Converting all elements in json map file.
     */
    private OverlapSceneReader loadComposite(final CompositeVO rootComposite) {
        if (rootComposite.layers != null) {
            rootComposite.layers.stream().forEach(layer -> GameObjectUtil.instance.addLayer(layer.layerName));
        }
        return registerImages(rootComposite.sImages)
                .registerLights(rootComposite)
                .registerEffects(rootComposite)
                .registerLabels(rootComposite)
                .registerAnimations(rootComposite)
                .registerImage9patchs(rootComposite)
                .registerTextBoxVO(rootComposite)
                .registerSelectBoxVO(rootComposite)
                .registerSpineVO(rootComposite)
                .registerSpriterVO(rootComposite)
                .loadOtherComposites(rootComposite);
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

    /**
     * Registration object in Box2D world and return list for rendering suppose.
     */
    private OverlapSceneReader registerImages(final ArrayList<SimpleImageVO> sImages) {
        final ComponentLoader registerComponent = new ComponentLoader();

        components.addAll(sImages.stream()
                .map(registerComponent::register)
                .collect(Collectors.toList()));

        return this;
    }

    /**
     * Register effects.
     */
    private OverlapSceneReader registerEffects(final CompositeVO rootComposite) {
        rootComposite.sParticleEffects.stream()
                .forEach(e -> components.add(register(new ParticleEffectsLoader(), e)));

        return this;
    }

    /**
     * Register lights.
     */
    private OverlapSceneReader registerLights(final CompositeVO rootComposite) {
        rootComposite.sLights.stream()
                .forEach(e -> register(new LightsLoader(), e));
        return this;
    }

    /**
     * Register lights.
     */
    private OverlapSceneReader registerLabels(final CompositeVO rootComposite) {
        rootComposite.sLabels.forEach(e -> components.add(register(new LabelsLoader(), e)));
        return this;
    }

    /**
     * Register animations.
     */
    private OverlapSceneReader registerAnimations(final CompositeVO rootComposite) {
        rootComposite.sSpriteAnimations.forEach(e -> components.add(register(new SpriteAnimationsLoader(), e)));
        return this;
    }

    /**
     * Register Image9patchs.
     */
    private OverlapSceneReader registerImage9patchs(final CompositeVO rootComposite) {
        if (rootComposite.sImage9patchs.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Register TextBoxVO.
     */
    private OverlapSceneReader registerTextBoxVO(final CompositeVO rootComposite) {
        if (rootComposite.sTextBox.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Register SelectBoxVO.
     */
    private OverlapSceneReader registerSelectBoxVO(final CompositeVO rootComposite) {
        if (rootComposite.sSelectBoxes.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Register SpineVO.
     */
    private OverlapSceneReader registerSpineVO(final CompositeVO rootComposite) {
        if (rootComposite.sSpineAnimations.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Register SpriterVO.
     */
    private OverlapSceneReader registerSpriterVO(final CompositeVO rootComposite) {
        if (rootComposite.sSpriterAnimations.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Loading composistes.
     */
    private OverlapSceneReader loadOtherComposites(final CompositeVO rootComposite) {
        rootComposite.sComposites.forEach(e -> this.loadComposite(e.composite, e.originX, e.originY, e.x, e.y, e.rotation));
        return this;
    }

}
