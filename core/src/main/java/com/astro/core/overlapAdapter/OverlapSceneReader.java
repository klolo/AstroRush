package com.astro.core.overlapAdapter;

import com.astro.core.objects.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.ResourceManager;

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
    private CompositeVO composite;

    /**
     *
     * @param scenePath
     */
    public OverlapSceneReader(final String scenePath) {
        this.scenePath = scenePath;
    }

    /**
     *
     */
    public OverlapSceneReader loadScene() {
        FileHandle file = Gdx.files.internal(scenePath);
        composite = new Json().fromJson(SceneVO.class, file.readString()).composite;
        return this;
    }

    /**
     * Registration object in Box2D world and return list for rendering suppose.
     */
    public List<IGameObject> registerComponents() {
        List<IGameObject> result = new ArrayList<>();
        RegisterComponent registerComponent = new RegisterComponent();
        result.addAll(composite.sImages.stream()
                .map(registerComponent::register)
                .collect(Collectors.toList()));
        return result;
    }
}
