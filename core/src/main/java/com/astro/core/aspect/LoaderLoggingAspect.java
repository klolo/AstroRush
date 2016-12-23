package com.astro.core.aspect;

import com.uwsoft.editor.renderer.data.MainItemVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging while loading components.
 */
@Slf4j
@Component
public class LoaderLoggingAspect {

    public void logBefore(final JoinPoint joinPoint) {
        try {
            final MainItemVO mainItemVO = (MainItemVO) joinPoint.getArgs()[0];

            LOGGER.info("** loading: itemName={}, itemIdentifier={}, x={}, y={}, layer={} **",
                    mainItemVO.itemName,
                    mainItemVO.itemIdentifier,
                    mainItemVO.x,
                    mainItemVO.x,
                    mainItemVO.layerName
            );
        }
        catch (final ClassCastException e) {
            LOGGER.error("Error while logging", e);
        }
    }

}
