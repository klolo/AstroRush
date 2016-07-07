package com.astro.core.overlap_runtime.loaders;

import com.astro.core.objects.LabelObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.overlap_runtime.converters.LabelVOToIGameObjectConverter;
import com.astro.core.storage.GameResources;
import com.uwsoft.editor.renderer.data.LabelVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Text object on the screen.
 */
@Slf4j
public class LabelsLoader implements ILoader<LabelVO>, ApplicationContextAware {

    @Autowired
    @Setter
    private ApplicationContext applicationContext;

    @Override
    public IGameObject register(final LabelVO labelVO) {
        log.info("[label] {}", labelVO.text);

        final LabelObject result = applicationContext.getBean(LabelObject.class);
        result.setBitmapFont(GameResources.instance.getResourceManager().getBitmapFont(labelVO.style, labelVO.size));
        result.getFont().setColor(labelVO.tint[0], labelVO.tint[1], labelVO.tint[2], labelVO.tint[3]);
        result.setText(labelVO.text);

        return new LabelVOToIGameObjectConverter().convert(labelVO, result);
    }
}
