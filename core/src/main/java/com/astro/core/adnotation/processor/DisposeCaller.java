package com.astro.core.adnotation.processor;

import com.astro.core.adnotation.Dispose;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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
        LOGGER.info("dispose object: {}", object);
        try {
            final Method m = field.getType().getMethod("dispose");
            m.setAccessible(true);

            final Field innerField = object.getClass().getDeclaredField(field.getName());
            innerField.setAccessible(true);

            m.invoke(innerField.get(object));
        }
        catch (final NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Cannot call dispose", e);
        }
    }

}
