package com.astro.core.overlap_runtime.converters;

import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.SimpleImageVO;

/**
 * Rewrite SimpleImageVO data to IGameObject.
 */
public class SimpleImageVOToIGameObjectConverter extends  MainItemVOToIGameObjectConverter implements IConverter<SimpleImageVO> {

    @Override
    public IGameObject convert(SimpleImageVO imageVO, IGameObject result) {
        super.convert(imageVO,result);

        result.setName(imageVO.imageName);

        return result;
    }
}
