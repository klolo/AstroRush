package com.astro.core.overlap_runtime.converters;

import com.astro.core.objects.LabelObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.LabelVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;

/**
 * Rewrite LabelVO data to IGameObject.
 */
public class LabelVOToIGameObjectConverter extends MainItemVOToIGameObjectConverter implements IConverter<LabelVO> {

    @Override
    public IGameObject convert(LabelVO labelVO, IGameObject result) {
        super.convert(labelVO, result);
        ((LabelObject) result).setNationalizedMsg();

        result.getSprite().setBounds(
                labelVO.x,
                labelVO.y,
                labelVO.width,
                labelVO.height
        );

        return result;
    }
}
