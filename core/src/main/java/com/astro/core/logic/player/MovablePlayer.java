package com.astro.core.logic.player;

import com.astro.core.engine.interfaces.IObservedByCamera;
import com.astro.core.logic.common.LogicTimer;
import com.astro.core.observe.IKeyObserver;
import com.badlogic.gdx.Input;

public class MovablePlayer extends PlayerLogic implements IKeyObserver, IObservedByCamera {

    private final int MINIMAL_FLY_POWER_AMOUNT = 1;

    protected MovablePlayer() {
        playerData.cameraManager.setObservedObject(this);
    }

    @Override
    public void update(final float diff) {
        super.update(diff);

        if (playerData.flyPowerAmount < playerData.startFlyPowerAmount) {
            playerData.flyPowerAmount += 0.1;
        }
    }

    @Override
    public void keyPressEvent(final int keyCode) {
        byte horizontalForce = 0;

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
        playerData.physicBody.setLinearVelocity(horizontalForce * PlayerData.HORIZONTAL_FORCE_STRENGHT,
                playerData.physicBody.getLinearVelocity().y);
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

    private void jump() {
        decreaseFlyPowerAmount();
        if (playerData.flyPowerAmount <= MINIMAL_FLY_POWER_AMOUNT) {
            return;
        }

        if (playerData.physicBody.getLinearVelocity().y < playerData.settings.maxYVelocity) {
            playerData.physicBody.applyForceToCenter(0, playerData.settings.maxYVelocity, false);
        }

        playerData.standOnThePlatform = false;
    }

    private void decreaseFlyPowerAmount() {
        if (playerData.flyPowerAmount > MINIMAL_FLY_POWER_AMOUNT) {
            playerData.flyPowerAmount -= 1;
        }
        else {
            playerData.flyPowerAmount = MINIMAL_FLY_POWER_AMOUNT;
        }
    }

    private void shoot() {
        if (playerData.canShoot) {
            runUnBlockShootTimer();
            children.add(playerData.fireBehavior.onFire());
            playerData.gameObject.getData().setHasChild(true);
        }
    }

    private void runUnBlockShootTimer() {
        if (playerData.watchers.get(WatchersID.SHOOT_UNBLOCKER) == null) {
            playerData.shootTimer = new LogicTimer<>(playerData::setCanShoot, true, playerData.fireBehavior.getFiringSpeed());
            playerData.watchers.put(WatchersID.SHOOT_UNBLOCKER, playerData.shootTimer);
        }
        else {
            playerData.watchers.get(WatchersID.SHOOT_UNBLOCKER).reset();
        }

        playerData.canShoot = false;
    }

    @Override
    public float getPositionX() {
        return playerData.physicBody.getPosition().x;
    }

    @Override
    public float getPositionY() {
        return playerData.physicBody.getPosition().y;
    }
}
