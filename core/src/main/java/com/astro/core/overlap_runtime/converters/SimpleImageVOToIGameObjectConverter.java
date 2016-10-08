package com.astro.core.overlap_runtime.converters;

import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import org.springframework.stereotype.Component;

/**
 * Rewrite SimpleImageVO data to IGameObject.
 */
@Component
public class SimpleImageVOToIGameObjectConverter extends MainItemVOToIGameObjectConverter implements IConverter<SimpleImageVO> {

    @Override
    public IGameObject convert(final SimpleImageVO imageVO, final IGameObject result) {
        super.convert(imageVO, result);
        result.getData().setName(imageVO.imageName);

        return result;
    }
}
