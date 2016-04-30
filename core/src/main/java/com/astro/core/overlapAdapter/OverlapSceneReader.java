package com.astro.core.overlapAdapter;

import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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

    /**
     * Opens and read json map file.
     */
    public OverlapSceneReader loadScene() {
        FileHandle file = Gdx.files.internal(scenePath);

        CompositeVO rootComposite = new Json().fromJson(SceneVO.class, file.readString()).composite;
        loadComposite(rootComposite, 0.0f, 0.0f, 0.0f);
        return this;
    }

    /**
     * Generic method for reading different kind of elements.
     */
    <T> IGameObject register(ILoader<T> loader, T element) {
        return loader.register(element);
    }

    /**
     * Converting all elements in json map file.
     */
    private OverlapSceneReader loadComposite(CompositeVO rootComposite, float parentX, float parentY, float rotation) {
        return registerImages(rootComposite.sImages, parentX, parentY, rotation)
                .registerLights(rootComposite)
                .registerEffects(rootComposite)
                .registerLabels(rootComposite)
                .loadOtherComposites(rootComposite);
    }

    /**
     * Registration object in Box2D world and return list for rendering suppose.
     */
    public OverlapSceneReader registerImages(ArrayList<SimpleImageVO> sImages, float offsetX, float offsetY, float rotation) {
        ComponentLoader registerComponent = new ComponentLoader();
        registerComponent.setParentX(offsetX);
        registerComponent.setParentY(offsetY);
        registerComponent.setParentRotate(rotation);

        components.addAll(sImages.stream()
                .map(registerComponent::register)
                .collect(Collectors.toList()));
        return this;
    }

    /**
     * Register effects.
     */
    public OverlapSceneReader registerEffects(CompositeVO rootComposite) {
        rootComposite.sParticleEffects.stream()
                .forEach(e -> components.add(register(new ParticleEffectsLoader(), e)));

        return this;
    }


    /**
     * Register lights.
     */
    public OverlapSceneReader registerLights(CompositeVO rootComposite) {
        rootComposite.sLights.stream()
                .forEach(e -> register(new LightsLoader(), e));
        return this;
    }


    /**
     * Register lights.
     */
    public OverlapSceneReader registerLabels(CompositeVO rootComposite) {
        rootComposite.sLabels.forEach(e -> components.add(register(new LabelsLoader(), e)));
        return this;
    }


    /**
     * Loading composistes.
     */
    private OverlapSceneReader loadOtherComposites(CompositeVO rootComposite) {
        rootComposite.sComposites.forEach(e -> this.loadComposite(e.composite, e.x, e.y, e.rotation));
        return this;
    }

}
