package com.astro.core.aspect;

import com.uwsoft.editor.renderer.data.MainItemVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

/**
 * Aspect for logging while loading components.
 */
@Slf4j
public class LoaderLoggingAspect {

    public void logBefore(final JoinPoint joinPoint) {
        try {
            final MainItemVO mainItemVO = (MainItemVO) joinPoint.getArgs()[0];

            log.info("*** loading object: itemName={}, itemIdentifier={}, x={}, y={}, layer={} ***",
                    mainItemVO.itemName,
                    mainItemVO.itemIdentifier,
                    mainItemVO.x,
                    mainItemVO.x,
                    mainItemVO.layerName
            );
        }
        catch (final ClassCastException e) {
            log.error("Error while logging", e);
        }
    }

}
