package com.astro.core.overlap_runtime.converters;

import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.MainItemVO;

/**
 * Created by kamil on 01.05.16.
 */
public class MainItemVOToIGameObjectConverter {

    public IGameObject convert(MainItemVO input, IGameObject result) {
        result.getSprite().setOrigin(
                input.originX,
                input.originY
        );

        result.getSprite().setScale(
                input.scaleX,
                input.scaleY
        );

        result.getSprite().setPosition(
                input.x,
                input.y
        );

        result.getSprite().setRotation(input.rotation);
        result.getSprite().setColor(input.tint[0], input.tint[1], input.tint[2], input.tint[3]);
        result.setLayerID(input.layerName);
        return result;
    }
}
