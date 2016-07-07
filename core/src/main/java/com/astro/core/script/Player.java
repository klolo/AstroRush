package com.astro.core.script;

import com.astro.core.engine.base.GameEngine;
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
public class Player implements ILogic, IKeyObserver, IObservedByCamera {

    /**
     * ID of the player.
     */
    public static final String IDENTIFIER = "player";


    public PlayerData playerData;

    /**
     * -Register player in the KeyObserver
     * -Set Player as followed object by camera
     * -create collision processor
     * -create all watcher
     */
    public Player() {
        log.info("Creating player");
        playerData = GameEngine.getApplicationContext().getBean(PlayerData.class);

        KeyObserve.instance.register(this);

        playerData.cameraManager.setObservedObject(this);
        playerData.collisionProcessor = new CollisionProcessor(this);
        playerData.watchers = new WatchersCreator(this).init().getWatchers();
    }

    /**
     * Set data loaded from json, which contains physics information and base animation.
     */
    public void setGameObject(final IGameObject gameObject) {
        log.info("Set game object player");
        playerData.playerPopupMsg.initLabel();
        playerData.gameObject = gameObject;

        gameObject.getData().setCollisionConsumer(playerData.collisionProcessor::processCollision);
        playerData.settings.playerHeight =
                ((AnimationObject) gameObject).getAnimation().getKeyFrames()[0].getRegionHeight() / playerData.settings.pixelPerMeter;

        playerData.body = gameObject.getData().getBody();
        playerData.body.setFixedRotation(true);

        playerData.graphics = new PlayerGraphics((AnimationObject) gameObject);
    }

    @Override
    public void onPause() {
        playerData.cameraManager.setObservedObject(null);
        KeyObserve.instance.unregister(this);
    }

    @Override
    public void onResume() {
        playerData.cameraManager.setObservedObject(this);
        KeyObserve.instance.register(this);
    }

    @Override
    public void update(final float diff) {
        updatePosition();
        playerData.playerPopupMsg.update(diff);
        playerData.watchers.values().forEach(w -> w.update(diff));
        playerData.fireBehavior.update(playerData.posX, playerData.posY);
    }

    /**
     * Update player position.
     */
    private void updatePosition() {
        playerData.posX = playerData.graphics.getRunAnimation().getData().getSprite().getX();
        playerData.posY = playerData.graphics.getRunAnimation().getData().getSprite().getY();

        float newX = playerData.body.getPosition().x;
        float newY = playerData.body.getPosition().y;

        playerData.state = playerData.state.getState(playerData.posX, playerData.posY, newX, newY, playerData.state);
        if (playerData.standOnThePlatform) {
            playerData.state = PlayerState.STAND;
        }
        playerData.graphics.getRunAnimation().setRenderingInScript(!playerData.state.isRun());

        if (playerData.body.getPosition().y > playerData.settings.maxYPosition) {
            playerData.body.setTransform(newX, playerData.settings.maxYPosition, 0);
        }
        playerData.graphics.getRunAnimation().getData().getSprite().setPosition(newX, newY);
        playerData.playerPopupMsg.setPos(newX, newY + 2 * playerData.settings.playerHeight);
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

        playerData.watchers.get(WatchersID.INACTIVE_PLAYER).reset();
        playerData.body.setLinearVelocity(horizontalForce * playerData.HORIZONTAL_FORCE_STRENGHT, playerData.body.getLinearVelocity().y);
    }

    private void shoot() {
        log.info("shoot");

        children.add(playerData.fireBehavior.onFire());
        playerData.gameObject.getData().setHasChild(true);
    }

    private void leftKeyEvent() {
        playerData.graphics.getRunAnimation().getData().setFlipX(true);
        playerData.standOnThePlatform = false;

        playerData.state = playerData.state.getPlayerStateAfterLeftKey();
        playerData.watchers.get(WatchersID.STOP_PLAYER_ON_PLATFORM).setStopped(false);
        playerData.fireBehavior.setPlayerState(playerData.state);
    }

    private void rightKeyEvent() {
        playerData.graphics.getRunAnimation().getData().setFlipX(false);
        playerData.standOnThePlatform = false;

        playerData.state = playerData.state.getPlayerStateAfterRightKey();
        playerData.watchers.get(WatchersID.STOP_PLAYER_ON_PLATFORM).setStopped(false);
        playerData.fireBehavior.setPlayerState(playerData.state);
    }

    /**
     * Called on Shift pressed.
     */
    private void processInterAct() {
        if (playerData.interactObject != null) {
            playerData.interactObject.interact();
            playerData.interactObject = null;
        }
    }

    /**
     * Process player jump.
     */
    private void jump() {
        if (playerData.body.getLinearVelocity().y < playerData.settings.maxYVelocity) {
            playerData.body.applyForceToCenter(0, playerData.settings.maxYVelocity, false);
        }

        playerData.standOnThePlatform = false;
    }

    @Override
    public void additionalRender(final OrthographicCamera cam, float delta) {
        playerData.playerPopupMsg.show(cam, delta);

        if (playerData.graphics.getRunAnimation().isRenderingInScript()) {
            IGameObject gfxObject = playerData.graphics.getTextureBasedOnState(playerData.state);
            gfxObject.getData().getSprite().setPosition(playerData.posX, playerData.posY);

            if (playerData.state.isFly()) {
                (gfxObject).getData().setFlipX(playerData.state == PlayerState.FLY_LEFT);
            }

            gfxObject.show(cam, delta);
        }
    }

    /**
     * Adding point to player
     */
    public void addPoints(int amount) {
        playerData.points += amount;
    }

    /**
     * Return all player points.
     */
    public int getPoints() {
        return playerData.points;
    }

    /**
     * Return player X position.
     */
    @Override
    public float getPositionX() {
        return playerData.body.getPosition().x;
    }

    /**
     * Return player Y position.
     */
    @Override
    public float getPositionY() {
        return playerData.body.getPosition().y;
    }

    /**
     * Decrease player life amount.
     */
    public void decreaseLive(final int amount) {
        log.info("Losing live amount:{}, current amount: {}", amount, playerData.liveAmount);
        playerData.liveAmount -= amount;

        if (playerData.liveAmount < 0) {
            log.error("player is dead");
            playerData.isDead = true;
        }
    }

    /**
     * Decrease player life amount.
     */
    public void addLive(final int amount) {
        log.info("Add live amount:{}, current amount: {}", amount, playerData.liveAmount);
        if (playerData.liveAmount + amount < playerData.startLiveAmount) {
            playerData.liveAmount += amount;
        }
        else {
            playerData.liveAmount = playerData.startLiveAmount;
        }
    }
}
