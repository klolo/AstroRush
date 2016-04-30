package com.astro.core.overlapAdapter;

import com.astro.core.objects.LabelObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.uwsoft.editor.renderer.data.LabelVO;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 30.04.16.
 */
@Slf4j
public class LabelsLoader implements ILoader<LabelVO> {

    /**
     *
     */
    public LabelsLoader() {
        resourceManager.initAllResources();
    }

    @Override
    public IGameObject register(LabelVO labelVO) {
        log.info("[register label] text: {}", labelVO.text);
        LabelObject result = new LabelObject(resourceManager.getBitmapFont(labelVO.style, labelVO.size));
        result.setText(labelVO.text);
        result.getSprite().setColor(labelVO.tint[0], labelVO.tint[1], labelVO.tint[2], labelVO.tint[3]);

        result.getSprite().setPosition(
                labelVO.x,
                labelVO.y
        );

        result.getSprite().setBounds(
                labelVO.x,
                labelVO.y,
                labelVO.width,
                labelVO.height
        );
        result.getSprite().setOrigin(
                labelVO.originX,
                labelVO.originY
        );
        result.getSprite().setScale(
                labelVO.scaleX,
                labelVO.scaleY
        );

        result.getFont().setColor(labelVO.tint[0], labelVO.tint[1], labelVO.tint[2], labelVO.tint[3]);
        result.getSprite().setRotation(labelVO.rotation);
        return result;
    }
}
