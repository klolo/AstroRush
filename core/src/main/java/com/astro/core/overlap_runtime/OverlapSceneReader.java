package com.astro.core.overlap_runtime;

import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.engine.stage.GameObjectUtil;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.loaders.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.common.base.Preconditions;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Overlap 2d map loader.
 */
@Slf4j
public class OverlapSceneReader {

    @Setter
    @Autowired
    private PhysicsEngine physicsEngine;

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
        Preconditions.checkArgument(sImages != null, "Object sImages for register should not be null");
        final ComponentLoader registerComponent = new ComponentLoader(physicsEngine);

        components.addAll(sImages.stream()
                .map(registerComponent::register)
                .collect(Collectors.toList()));

        return this;
    }

    /**
     * Register effects.
     */
    private OverlapSceneReader registerEffects(final CompositeVO rootComposite) {
        Preconditions.checkArgument(rootComposite != null, "Object rootComposite for register should not be null");
        rootComposite.sParticleEffects.stream()
                .forEach(e -> components.add(register(new ParticleEffectsLoader(), e)));

        return this;
    }

    /**
     * Register lights.
     */
    private OverlapSceneReader registerLights(final CompositeVO rootComposite) {
        Preconditions.checkArgument(rootComposite != null, "Object rootComposite for register should not be null");
        rootComposite.sLights.stream()
                .forEach(e -> register(new LightsLoader(physicsEngine), e));
        return this;
    }

    /**
     * Register lights.
     */
    private OverlapSceneReader registerLabels(final CompositeVO labels) {
        Preconditions.checkArgument(labels != null, "Object labels for register should not be null");
        labels.sLabels.forEach(e -> components.add(register(new LabelsLoader(), e)));
        return this;
    }

    /**
     * Register animations.
     */
    private OverlapSceneReader registerAnimations(final CompositeVO animations) {
        Preconditions.checkArgument(animations != null, "Object animations for register should not be null");
        animations.sSpriteAnimations.forEach(e -> components.add(register(new SpriteAnimationsLoader(physicsEngine), e)));
        return this;
    }

    /**
     * Register Image9patchs.
     */
    private OverlapSceneReader registerImage9patchs(final CompositeVO image9patchs) {
        Preconditions.checkArgument(image9patchs != null, "Object image9patchs for register should not be null");
        if (image9patchs.sImage9patchs.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Register TextBoxVO.
     */
    private OverlapSceneReader registerTextBoxVO(final CompositeVO textBoxVO) {
        Preconditions.checkArgument(textBoxVO != null, "Object textBoxVO for register should not be null");
        if (textBoxVO.sTextBox.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Register SelectBoxVO.
     */
    private OverlapSceneReader registerSelectBoxVO(final CompositeVO selectBoxVO) {
        Preconditions.checkArgument(selectBoxVO != null, "Object selectBoxVO for register should not be null");
        if (selectBoxVO.sSelectBoxes.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Register SpineVO.
     */
    private OverlapSceneReader registerSpineVO(final CompositeVO spineVO) {
        Preconditions.checkArgument(spineVO != null, "Object spineVO for register should not be null");
        if (spineVO.sSpineAnimations.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Register SpriterVO.
     */
    private OverlapSceneReader registerSpriterVO(final CompositeVO spriterVO) {
        Preconditions.checkArgument(spriterVO != null, "Object spriterVO for register should not be null");
        if (spriterVO.sSpriterAnimations.size() > 0) {
            log.error(UNKNOWN_COMPONENT_MSG);
        }
        return this;
    }

    /**
     * Loading composistes.
     */
    private OverlapSceneReader loadOtherComposites(final CompositeVO otherComposites) {
        Preconditions.checkArgument(otherComposites != null, "Object rootComposite for register should not be null");
        otherComposites.sComposites.forEach(e -> this.loadComposite(e.composite, e.originX, e.originY, e.x, e.y, e.rotation));
        return this;
    }

}
