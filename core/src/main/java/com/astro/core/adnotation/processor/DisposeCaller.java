package com.astro.core.adnotation.processor;

import com.astro.core.adnotation.Dispose;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Called method dispose on field which have annotation Dispose.
 */
@Slf4j
public class DisposeCaller {

    /**
     * Method looking for Field annotated by Dispose and call on it method dispose.
     */
    public void callDispose(final Object object) {
        ReflectionHelper.getInheritedPrivateFields(object.getClass())
                .forEach(field -> Arrays.stream(field.getAnnotations())
                        .filter(annotation -> annotation instanceof Dispose)
                        .forEach(annotation -> callDispose(field, object))
                );
    }

    /**
     * Call method dispose on field.
     */
    private void callDispose(final Field field, final Object object) {
        log.info("dispose object: {}", object);
        try {
            Method m = field.getType().getMethod("dispose");
            m.setAccessible(true);

            Field innerField = object.getClass().getDeclaredField(field.getName());
            innerField.setAccessible(true);

            m.invoke(innerField.get(object));
        }
        catch (final Exception e) {
            log.error("Cannot call dispose", e);
        }
    }

}
