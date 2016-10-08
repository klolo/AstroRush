package com.astro.core.overlap_runtime.converters;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.uwsoft.editor.renderer.data.MainItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * Based converter for overlap runtime loader. It sets all base attributes in GameObject.
 */
@Slf4j
@Component
public class MainItemVOToIGameObjectConverter {

    @GameProperty("game.script.package")
    private String scriptPackage = "";

    public MainItemVOToIGameObjectConverter() {
        PropertyInjector.instance.inject(this);
    }

    public IGameObject convert(final MainItemVO input, final IGameObject result) {
        result.getData().getSprite().setOrigin(input.originX, input.originY);
        result.getData().getSprite().setScale(input.scaleX, input.scaleY);
        result.getData().getSprite().setPosition(input.x, input.y);

        result.getData().setItemIdentifier(input.itemIdentifier);

        /**
         * Parsing custom vars from editor. Example from json:
         * "customVars": "param1:val1;param2:val2",
         */
        Optional.of(input.customVars)
                .ifPresent(e -> Arrays.asList(e.split(";"))
                        .forEach(a -> setArgs(a, result)));

        result.getData().getSprite().setRotation(input.rotation);
        result.getData().getSprite().setColor(input.tint[0], input.tint[1], input.tint[2], input.tint[3]);
        result.getData().setLayerID(input.layerName);
        return result;
    }

    /**
     * Settings args in object
     */
    private void setArgs(final String arg, final IGameObject result) {
        final String[] data = arg.split(":");
        if (data.length == 2) {
            LOGGER.info("[Custom vars] {}={}", data[0], data[1]);

            if (IGameObject.LOGIC_VARS.equals(data[0])) {
                final String className = scriptPackage + "." + data[1];
                setGameLogic(className, result);
            }
            result.getData().getCustomVariables().put(data[0], data[1]);
        }
    }

    /**
     * Create and put game logic object by class name.
     */
    private void setGameLogic(final String className, final IGameObject result) {
        try {
            LOGGER.info("Setting logic: {}", className);
            final Class clazz = Class.forName(className);
            final ILogic logic = (ILogic) clazz.newInstance();
            PropertyInjector.instance.inject(logic);
            logic.setGameObject(result);
            result.getData().setLogic(logic);
        }
        catch (final ClassNotFoundException exception) {
            LOGGER.error("Class not found:" + className);
        }
        catch (final InstantiationException | IllegalAccessException exception) {
            LOGGER.error("Error in initializing logic:" + className, exception);
        }
    }

}
