package com.astro.core.script.player;

import com.astro.core.script.Player;
import com.astro.core.script.util.LogicTimer;
import lombok.Getter;

import java.util.HashMap;

/**
 * Created by kamil on 19.05.16.
 */
public class WatchersCreator {

    private Player player;

    @Getter
    private HashMap<WatchersID, LogicTimer> watchers = new HashMap<>();

    public WatchersCreator(Player player) {
        this.player = player;
    }

    public WatchersCreator init() {
        LogicTimer inactiveMsgWatcher =
                new LogicTimer<>(
                        player.getSettings().inactivePLayerMessage,
                        player.getPlayerPopupMsg()::addMessagesToQueue,
                        player.getSettings().inactiveMsgTime
                );
        watchers.put(WatchersID.INACTIVE_PLAYER, inactiveMsgWatcher);

        LogicTimer interactionWatcher =
                new LogicTimer<>(
                        null,
                        player::setInteractObject,
                        player.getSettings().interactWithObjectTime
                );
        watchers.put(WatchersID.INTERACT_WITH_OTHER_OBJECT, interactionWatcher);

        LogicTimer stopPLayerWatcher =
                new LogicTimer<>(
                        true,
                        player::setStandOnThePlatform,
                        .7f// FIXME
                );
        stopPLayerWatcher.setLooped(false);
        stopPLayerWatcher.setStopped(true);
        watchers.put(WatchersID.STOP_PLAYER_ON_PLATFORM, stopPLayerWatcher);
        return this;
    }


}
