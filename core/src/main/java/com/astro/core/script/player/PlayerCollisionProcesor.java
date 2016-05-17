package com.astro.core.script.player;

import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.script.Player;
import com.astro.core.script.Point;
import com.astro.core.script.Sheep;
import com.astro.core.script.Switch;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Process Player collision with other object.
 */
@Slf4j
public class PlayerCollisionProcesor {

    /**
     * Instance of the player.
     */
    private Player player;

    /**
     * Requeired player object.
     */
    public PlayerCollisionProcesor(Player player) {
        this.player = player;
    }

    /**
     * Calling method collision defined by script class and using refelction.
     */
    public void processCollision(final IGameObject collidatedObject) {
        try {
            Method m = getClass().getMethod("collision", (Class) collidatedObject.getData().getLogic().getClass());
            m.setAccessible(true);
            m.invoke(this, collidatedObject.getData().getLogic());
        }
        catch (final Exception e) {
            log.warn("Cannot find method");
        }
    }

    /**
     * Collision with monet or other object with gives points.
     */
    public void collision(final Point point) {
        player.getPlayerPopupMsg().addMessagesToQueue(point.getPlayerMsg());
        player.addPoints(Integer.valueOf(point.getPlayerMsg()));
    }

    /**
     * Test collision with enemy. Should be remove on final version.
     */
    public void collision(final Sheep sheep) {
        player.getPlayerPopupMsg().addMessagesToQueue("ouh!");
    }

    /**
     * Interact with switch
     */
    public void collision(final Switch s) {
        player.getPlayerPopupMsg().addMessagesToQueue("Press shift to interact");
        player.setInteractObject(s);
    }


}
