package com.astro.game.overlap_runtime.converters;

import com.astro.game.objects.LabelObject;
import com.astro.game.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.LabelVO;
import org.springframework.stereotype.Component;

/**
 * Rewrite LabelVO data to IGameObject.
 */
@Component
public class LabelVOToIGameObjectConverter extends MainItemVOToIGameObjectConverter implements IConverter<LabelVO> {

    @Override
    public IGameObject convert(LabelVO labelVO, IGameObject result) {
        super.convert(labelVO, result);
        ((LabelObject) result).setNationalizedMsg();

        result.getData().getSprite().setBounds(
                labelVO.x,
                labelVO.y,
                labelVO.width,
                labelVO.height
        );

        return result;
    }
}
