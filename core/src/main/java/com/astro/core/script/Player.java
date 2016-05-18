package com.astro.core.script;

import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.interfaces.IObservedByCamera;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import com.astro.core.script.player.*;
import com.astro.core.script.util.LogicTimer;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.extern.slf4j.Slf4j;

/**
 * Logic of the Player.
 */
@Slf4j
public class Player extends PlayerData implements ILogic, IKeyObserver, IObservedByCamera {

    /**
     * ID of the player.
     */
    public static final String IDENTIFIER = "player";

    public Player() {
        KeyObserve.instance.register(this);
        CameraManager.instance.setObservedObject(this);
        collisionProcesor = new PlayerCollisionProcesor(this);

        initWatchers();
    }

    /**
     * Create all watchers.
     */
    private void initWatchers() {
        LogicTimer inactiveMsgWatcher =
                new LogicTimer<>(
                        settings.inactivePLayerMessage,
                        playerPopupMsg::addMessagesToQueue,
                        settings.inactiveMsgTime
                );
        watchers.put(WatchersID.INACTIVE_PLAYER, inactiveMsgWatcher);

        LogicTimer interactionWatcher =
                new LogicTimer<>(
                        null,
                        this::setInteractObject,
                        settings.interactWithObjectTime
                );
        watchers.put(WatchersID.INTERACT_WITH_OTHER_OBJECT, interactionWatcher);
    }

    public void setGameObject(IGameObject runAnimation) {
        runAnimation.getData().setCollisionConsumer(collisionProcesor::processCollision);
        settings.playerHeight =
                ((AnimationObject) runAnimation).getAnimation().getKeyFrames()[0].getRegionHeight() / settings.PIXEL_PER_METER;

        body = runAnimation.getData().getBody();
        body.setFixedRotation(true);

        graphics = new PlayerGraphics((AnimationObject) runAnimation);
    }

    @Override
    public void update(float diff) {
        updatePosition();
        playerPopupMsg.update(diff);
        watchers.values().forEach(w -> w.update(diff));
    }

    /**
     * Update player position.
     */
    private void updatePosition() {
        float lastX = graphics.getRunAnimation().getData().getSprite().getX();
        float lastY = graphics.getRunAnimation().getData().getSprite().getY();

        float newX = body.getPosition().x;
        float newY = body.getPosition().y;

        state = state.getState(lastX, lastY, newX, newY);
        graphics.getRunAnimation().setRenderingInScript(!state.isRun());

        if (body.getPosition().y > settings.MAX_Y_POSITION) {
            body.setTransform(newX, settings.MAX_Y_POSITION, 0);
        }
        graphics.getRunAnimation().getData().getSprite().setPosition(newX, newY);
        playerPopupMsg.setPos(newX, newY + 2 * settings.playerHeight);
    }

    @Override
    public void keyPressEvent(int keyCode) {
        int horizontalForce = 0;
        if (Input.Keys.LEFT == keyCode) {
            horizontalForce -= 1;
            graphics.getRunAnimation().setFlipX(true);
        }
        else if (Input.Keys.RIGHT == keyCode) {
            horizontalForce += 1;
            graphics.getRunAnimation().setFlipX(false);
        }
        else if (Input.Keys.UP == keyCode) {
            jump();
        }
        else if (Input.Keys.SHIFT_LEFT == keyCode) {
            processInterAct();
        }

        watchers.get(WatchersID.INACTIVE_PLAYER).reset();
        body.setLinearVelocity(horizontalForce * 5, body.getLinearVelocity().y);
    }

    private void processInterAct() {
        if (interactObject != null) {
            interactObject.interact();
        }
    }

    private void jump() {
        if (body.getLinearVelocity().y < settings.MAX_Y_VELOCITY) {
            body.applyForceToCenter(0, settings.MAX_Y_VELOCITY, false);
        }
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }

    @Override
    public void additionalRender(final OrthographicCamera cam, float delta) {
        playerPopupMsg.show(cam, delta);

        if (graphics.getRunAnimation().isRenderingInScript()) {
            float posX = graphics.getRunAnimation().getData().getSprite().getX();
            float posY = graphics.getRunAnimation().getData().getSprite().getY();

            IGameObject gfxObject = graphics.getTextureBasedOnState(state);
            gfxObject.getData().getSprite().setPosition(posX, posY);

            if (state.isFly()) {
                ((TextureObject) gfxObject).setFlipX(state == PlayerState.FLY_LEFT);
            }

            gfxObject.show(cam, delta);
        }
    }

    /**
     * Adding point to player
     */
    public void addPoints(int amount) {
        points += amount;
    }

    /**
     * Return all player points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Return player X position.
     */
    @Override
    public float getPositionX() {
        return body.getPosition().x;
    }

    /**
     * Return player Y position.
     */
    @Override
    public float getPositionY() {
        return body.getPosition().y;
    }
}
