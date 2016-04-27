package com.astro.core.storage;

import com.astro.core.adnotation.GameProperty;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

/**
 * Used for automatic setting values from properties in  objected where are field marked by adnotation GameProperty.
 *
 * @see com.astro.core.adnotation.GameProperty
 */
@Slf4j
public class PropertyInjector {

    /**
     * Walking throw object and looking for adnotated field, and set value in marked field. Supported field type: string, int, float
     *
     * @param obj - object where values will be injected.
     */
    public PropertyInjector(Object obj) {
        log.debug("start");
        Arrays.stream(obj.getClass().getDeclaredFields()).forEach(
                field -> Arrays.stream(field.getAnnotations()).forEach(
                        annotation -> {
                            if (annotation instanceof GameProperty) {

                                try {
                                    GameProperty objProperty = (GameProperty) annotation;
                                    field.setAccessible(true);

                                    String propValue = PropertiesReader.instance.getProperty(objProperty.value());
                                    if (field.getType().isPrimitive()) {

                                        if (field.getType() == Integer.TYPE) {
                                            field.set(obj, Integer.valueOf(propValue));
                                        }
                                        else if (field.getType() == Float.TYPE) {
                                            field.set(obj, Float.valueOf(propValue));
                                        }
                                        else if (field.getType() == Boolean.TYPE) {
                                            field.set(obj, Boolean.valueOf(propValue).booleanValue());
                                        }
                                        else {
                                            log.error("Incorrect type of field");
                                            return;
                                        }
                                    }
                                    else {
                                        field.set(obj, propValue);
                                    }

                                    log.info("Inject value: {}={}, in class: {}, by key: {}",
                                            field, propValue, obj.getClass(), objProperty.value());
                                }
                                catch (final Exception e) {
                                    log.error("Cannot set property", e);
                                }
                            }
                        }
                )
        );
    }
}
