package com.astro.core.script;

import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.interfaces.IObservedByCamera;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import com.astro.core.script.player.*;
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

    /**
     * -Register player in the KeyObserver
     * -Set Player as followed object by camera
     * -create collision processor
     * -create all watcher
     */
    public Player() {
        log.info("Creating player");
        KeyObserve.instance.register(this);
        CameraManager.instance.setObservedObject(this);
        collisionProcessor = new CollisionProcessor(this);
        watchers = new WatchersCreator(this).init().getWatchers();
    }

    /**
     * Set data loaded from json, which contains physics information and base animation.
     */
    public void setGameObject(final IGameObject gameObject) {
        log.info("Set game object player");
        playerPopupMsg.initLabel();
        this.gameObject = gameObject;

        gameObject.getData().setCollisionConsumer(collisionProcessor::processCollision);
        settings.playerHeight =
                ((AnimationObject) gameObject).getAnimation().getKeyFrames()[0].getRegionHeight() / settings.PIXEL_PER_METER;

        body = gameObject.getData().getBody();
        body.setFixedRotation(true);

        graphics = new PlayerGraphics((AnimationObject) gameObject);
    }

    @Override
    public void onPause() {
        CameraManager.instance.setObservedObject(null);
        KeyObserve.instance.unregister(this);
    }

    @Override
    public void onResume() {
        CameraManager.instance.setObservedObject(this);
        KeyObserve.instance.register(this);
    }

    @Override
    public void update(final float diff) {
        updatePosition();
        playerPopupMsg.update(diff);
        watchers.values().forEach(w -> w.update(diff));
        fireBehavior.update(posX, posY);
    }

    /**
     * Update player position.
     */
    private void updatePosition() {
        posX = graphics.getRunAnimation().getData().getSprite().getX();
        posY = graphics.getRunAnimation().getData().getSprite().getY();

        float newX = body.getPosition().x;
        float newY = body.getPosition().y;

        state = state.getState(posX, posY, newX, newY, state);
        if (standOnThePlatform) {
            state = PlayerState.STAND;
        }
        graphics.getRunAnimation().setRenderingInScript(!state.isRun());

        if (body.getPosition().y > settings.MAX_Y_POSITION) {
            body.setTransform(newX, settings.MAX_Y_POSITION, 0);
        }
        graphics.getRunAnimation().getData().getSprite().setPosition(newX, newY);
        playerPopupMsg.setPos(newX, newY + 2 * settings.playerHeight);
    }

    @Override
    public void keyPressEvent(final int keyCode) {
        int horizontalForce = 0;

        switch (keyCode) {
            case Input.Keys.LEFT: {
                horizontalForce -= 1;
                leftKeyEvent();
                break;
            }
            case Input.Keys.RIGHT: {
                horizontalForce += 1;
                rightKeyEvent();
                break;
            }
            case Input.Keys.UP: {
                jump();
                break;
            }
            case Input.Keys.SHIFT_LEFT: {
                processInterAct();
                break;
            }
            case Input.Keys.SPACE: {
                shoot();
                break;
            }
        }

        watchers.get(WatchersID.INACTIVE_PLAYER).reset();
        body.setLinearVelocity(horizontalForce * HORIZONTAL_FORCE_STRENGHT, body.getLinearVelocity().y);
    }

    private void shoot() {
        log.info("shoot");

        children.add(fireBehavior.onFire());
        gameObject.getData().setHasChild(true);
    }

    private void leftKeyEvent() {
        graphics.getRunAnimation().getData().setFlipX(true);
        standOnThePlatform = false;

        state = state.getPlayerStateAfterLeftKey();
        watchers.get(WatchersID.STOP_PLAYER_ON_PLATFORM).setStopped(false);
        fireBehavior.setPlayerState(state);
    }

    private void rightKeyEvent() {
        graphics.getRunAnimation().getData().setFlipX(false);
        standOnThePlatform = false;

        state = state.getPlayerStateAfterRightKey();
        watchers.get(WatchersID.STOP_PLAYER_ON_PLATFORM).setStopped(false);
        fireBehavior.setPlayerState(state);
    }

    /**
     * Called on Shift pressed.
     */
    private void processInterAct() {
        if (interactObject != null) {
            interactObject.interact();
            interactObject = null;
        }
    }

    /**
     * Process player jump.
     */
    private void jump() {
        if (body.getLinearVelocity().y < settings.MAX_Y_VELOCITY) {
            body.applyForceToCenter(0, settings.MAX_Y_VELOCITY, false);
        }

        standOnThePlatform = false;
    }

    @Override
    public void additionalRender(final OrthographicCamera cam, float delta) {
        playerPopupMsg.show(cam, delta);

        if (graphics.getRunAnimation().isRenderingInScript()) {
            IGameObject gfxObject = graphics.getTextureBasedOnState(state);
            gfxObject.getData().getSprite().setPosition(posX, posY);

            if (state.isFly()) {
                (gfxObject).getData().setFlipX(state == PlayerState.FLY_LEFT);
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

    /**
     * Decrease player life amount.
     */
    public void decreaseLive(final int amount) {
        log.info("Losing live amount:{}, current amount: {}", amount, liveAmount);
        liveAmount -= amount;

        if (liveAmount < 0) {
            log.error("player is dead");
            isDead = true;
        }
    }

    /**
     * Decrease player life amount.
     */
    public void addLive(final int amount) {
        log.info("Add live amount:{}, current amount: {}", amount, liveAmount);
        if (liveAmount + amount < startLiveAmount) {
            liveAmount += amount;
        }
        else {
            liveAmount = startLiveAmount;
        }
    }
}
