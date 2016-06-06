package com.astro.core.script.player;

import com.astro.core.script.Player;
import com.astro.core.script.util.LogicTimer;
import lombok.Getter;

import java.util.HashMap;


public class WatchersCreator {

    private Player player;

    @Getter
    private HashMap<WatchersID, LogicTimer> watchers = new HashMap<>();

    public WatchersCreator(Player player) {
        this.player = player;
    }

    public WatchersCreator init() {
        final LogicTimer inactiveMsgWatcher =
                new LogicTimer<>(
                        player.getSettings().inactivePLayerMessage,
                        player.getPlayerPopupMsg()::addMessagesToQueue,
                        player.getSettings().inactiveMsgTime
                );
        watchers.put(WatchersID.INACTIVE_PLAYER, inactiveMsgWatcher);

        final LogicTimer interactionWatcher =
                new LogicTimer<>(
                        null,
                        player::setInteractObject,
                        player.getSettings().interactWithObjectTime
                );
        watchers.put(WatchersID.INTERACT_WITH_OTHER_OBJECT, interactionWatcher);

        final LogicTimer stopPLayerWatcher =
                new LogicTimer<>(
                        true,
                        player::setStandOnThePlatform,
                        .7f// FIXME
                );
        stopPLayerWatcher.setLooped(false);
        stopPLayerWatcher.setStopped(true);
        watchers.put(WatchersID.STOP_PLAYER_ON_PLATFORM, stopPLayerWatcher);


        final LogicTimer decreaseLiveWatcher =
                new LogicTimer<>(
                        1,
                        player::decreaseLive,
                        1f
                );
        watchers.put(WatchersID.DECREASE_LIVE, decreaseLiveWatcher);
        return this;
    }


}
