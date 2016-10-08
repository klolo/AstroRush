package com.astro.game.script.player;

import com.astro.game.engine.physics.CollisionProcessResult;
import com.astro.game.objects.interfaces.IGameObject;
import com.astro.game.script.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Process Player collision with other object.
 */
@Slf4j
public class PlayerCollisionProcessor {

    /**
     * Instance of the player.
     */
    private Player player;

    /**
     * Requeired player object.
     */
    public PlayerCollisionProcessor(final Player player) {
        this.player = player;
    }

    /**
     * Calling method collision defined by script class and using refelction.
     */
    public CollisionProcessResult processCollision(final IGameObject collidatedObject) {
        try {
            Method m = getClass().getMethod("collision", (Class) collidatedObject.getData().getLogic().getClass());
            m.setAccessible(true);
            return (CollisionProcessResult) m.invoke(this, collidatedObject.getData().getLogic());
        }
        catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.warn("Cannot find method");
        }

        return null;
    }

    /**
     * Collision with monet or other object with gives points.
     */
    public CollisionProcessResult collision(final Point point) {
        player.playerData.getPlayerPopupMsg().addMessagesToQueue(point.getPlayerMsg());
        player.addPoints(Integer.valueOf(point.getPlayerMsg()));
        player.addLive(10);

        final CollisionProcessResult collisionProcessResult = new CollisionProcessResult();
        collisionProcessResult.setIgnoreCollision(true);
        return collisionProcessResult;
    }

    /**
     * Test collision with enemy. Should be remove on final version.
     */
    public CollisionProcessResult collision(final Enemy enemy) {
        player.playerData.getPlayerPopupMsg().addMessagesToQueue("ouh!");
        return new CollisionProcessResult();
    }

    /**
     * Interact with switch
     */
    public CollisionProcessResult collision(final Switch s) {
        player.playerData.getPlayerPopupMsg().addMessagesToQueue("Press shift to interact");
        player.playerData.setInteractObject(s);
        return new CollisionProcessResult();
    }

    /**
     * Interact with platform
     */
    public CollisionProcessResult collision(final MovingPlatform s) {
        player.playerData.setStandOnThePlatform(true);
        return new CollisionProcessResult();
    }

}
