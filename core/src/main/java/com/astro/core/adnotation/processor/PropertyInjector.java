package com.astro.core.adnotation.processor;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.Msg;
import com.astro.core.storage.PropertiesReader;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Used for automatic setting values from properties in  objected where are field marked by annotation GameProperty.
 *
 * @see com.astro.core.adnotation.GameProperty
 */
@Slf4j
public enum PropertyInjector {
    instance;

    /**
     * Walking throw object and looking for annotated field, and set value in marked field.
     * Supported field type: string, int, float
     *
     * @param obj - object where values will be injected.
     */
    public void inject(final Object obj) {
        ReflectionHelper.getInheritedPrivateFields(obj.getClass())
                .forEach(field -> Arrays.stream(field.getAnnotations())
                        .filter(this::isCorrectAnnotation)
                        .forEach(annotation -> processField(annotation, field, obj))
                );
    }

    /**
     * Check if field is annotated by correct annotation.
     */
    private boolean isCorrectAnnotation(final Annotation annotation) {
        return annotation instanceof GameProperty || annotation instanceof Msg;
    }

    /**
     * Setting value if field which have GameProperty annotation.
     */
    private void processField(final Annotation annotation, final Field field, final Object obj) {
        Preconditions.checkArgument(annotation != null, "Annotation is null");
        Preconditions.checkArgument(obj != null, "Object is null");
        Preconditions.checkArgument(field != null, "Field is null");

        try {
            field.setAccessible(true);
            String propValue;
            String fieldVal;

            if (annotation instanceof GameProperty) {
                final GameProperty objProperty = (GameProperty) annotation;
                fieldVal = objProperty.value();
                propValue = PropertiesReader.instance.getProperty(fieldVal);

            }
            else {
                final Msg objProperty = (Msg) annotation;
                fieldVal = objProperty.value();
                propValue = PropertiesReader.instance.getMsg(fieldVal);
            }

            LOGGER.debug("Injecting value: {}={}, in class: {}, by key: {}",
                    field, propValue, obj.getClass(), fieldVal);
            setFieldValue(obj, field, propValue);
        }
        catch (final IllegalAccessException e) {
            LOGGER.error("Cannot set property", e);
        }
    }

    /**
     * Setting value in field.
     *
     * @param obj       - object in which will be set value.
     * @param field     - field for setting
     * @param propValue - value of the field.
     */
    private void setFieldValue(final Object obj, final Field field, final String propValue) throws IllegalAccessException {
        if (field.getType().isPrimitive()) {
            if (field.getType() == Integer.TYPE) {
                field.set(obj, Integer.valueOf(propValue));
            }
            else if (field.getType() == Float.TYPE) {
                field.set(obj, Float.valueOf(propValue));
            } else if (field.getType() == Short.TYPE) {
                field.set(obj, Short.valueOf(propValue));
            } else if (field.getType() == Boolean.TYPE) {
                field.set(obj, Boolean.valueOf(propValue));
            } else {
                LOGGER.error("Incorrect type of field");
            }
        }
        else {
            field.set(obj, propValue);
        }
    }
}
