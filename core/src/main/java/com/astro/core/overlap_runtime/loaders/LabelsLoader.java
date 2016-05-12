package com.astro.core.overlap_runtime.loaders;

import com.astro.core.engine.base.GameResources;
import com.astro.core.objects.LabelObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.LabelVOToIGameObjectConverter;
import com.uwsoft.editor.renderer.data.LabelVO;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class LabelsLoader implements ILoader<LabelVO> {

    @Override
    public IGameObject register(LabelVO labelVO) {
        log.info("[register label] text: {}", labelVO.text);
        LabelObject result = new LabelObject(GameResources.instance.getResourceManager().getBitmapFont(labelVO.style, labelVO.size));

        result.getFont().setColor(labelVO.tint[0], labelVO.tint[1], labelVO.tint[2], labelVO.tint[3]);
        result.setText(labelVO.text);

        return new LabelVOToIGameObjectConverter().convert(labelVO, result);
    }
}
