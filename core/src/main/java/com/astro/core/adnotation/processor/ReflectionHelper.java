package com.astro.core.adnotation.processor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Util which helps in reflection process.
 */
final class ReflectionHelper {

    /**
     * Util class with private constructor.
     */
    private ReflectionHelper() {

    }

    /**
     * Collect all inherit fields.
     */
    static List<Field> getInheritedPrivateFields(Class<?> type) {
        List<Field> result = new ArrayList<>();

        Class<?> i = type;
        while (i != null && i != Object.class) {
            Arrays.stream(i.getDeclaredFields()).forEach(
                    field -> {
                        if (!field.isSynthetic()) {
                            result.add(field);
                        }
                    }
            );
            i = i.getSuperclass();
        }

        return result;
    }

}
