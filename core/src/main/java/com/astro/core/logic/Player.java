package com.astro.core.logic;

import com.astro.core.engine.base.GameEngine;
import com.astro.core.logic.player.MovablePlayer;
import com.astro.core.logic.player.PlayerCollisionProcessor;
import com.astro.core.logic.player.WatchersCreator;
import com.astro.core.observe.KeyObserve;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Player extends MovablePlayer {

    public static final String IDENTIFIER = "player";

    private KeyObserve keyObserve;

    /**
     * -Register player in the KeyObserver
     * -Set Player as followed object by camera
     * -create collision processor
     * -create all watcher
     */
    public Player() {
        registerInKeyObserver();
        playerData.playerCollisionProcessor = new PlayerCollisionProcessor(this);
        playerData.watchers = new WatchersCreator(this).init().getWatchers();
    }

    public void registerInKeyObserver() {
        keyObserve = GameEngine.getApplicationContext().getBean(KeyObserve.class);
        keyObserve.register(this);
    }

    @Override
    public void onPause() {
        playerData.cameraManager.setObservedObject(null);
        keyObserve.unregister(this);
    }

    @Override
    public void onResume() {
        playerData.cameraManager.setObservedObject(this);
        keyObserve.register(this);
    }

    public void addPoints(final int amount) {
        playerData.points += amount;
    }

    public void addPointsWithMessages(final int amount) {
        playerData.playerPopupMsg.addMessagesToQueue("+" + amount);
        playerData.points += amount;
    }

    public int getPoints() {
        return playerData.points;
    }

    public void decreaseLive(final int amount) {
        LOGGER.info("Losing live amount:{}, current amount: {}", amount, playerData.liveAmount);
        playerData.liveAmount -= amount;

        if (playerData.liveAmount < 0) {
            LOGGER.error("player is dead");
            playerData.isDead = true;
        }
    }

    public void addLive(final int amount) {
        LOGGER.info("Add live amount:{}, current amount: {}", amount, playerData.liveAmount);
        if (playerData.liveAmount + amount < playerData.startLiveAmount) {
            playerData.liveAmount += amount;
        } else {
            playerData.liveAmount = playerData.startLiveAmount;
        }
    }
}
