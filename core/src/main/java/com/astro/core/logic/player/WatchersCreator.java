package com.astro.core.logic.player;

import com.astro.core.logic.Player;
import com.astro.core.logic.util.LogicTimer;
import lombok.Getter;

import java.util.HashMap;


public class WatchersCreator {

    private Player player;

    @Getter
    private HashMap<WatchersID, LogicTimer> watchers = new HashMap<>();

    public WatchersCreator(final Player player) {
        this.player = player;
    }

    public WatchersCreator init() {
        final LogicTimer inactiveMsgWatcher =
                new LogicTimer<>(
                        player.playerData.getPlayerPopupMsg()::addMessagesToQueue,
                        player.playerData.getSettings().inactivePLayerMessage,
                        player.playerData.getSettings().inactiveMsgTime
                );
        watchers.put(WatchersID.INACTIVE_PLAYER, inactiveMsgWatcher);

        final LogicTimer interactionWatcher =
                new LogicTimer<>(player.playerData::setInteractObject, null, player.playerData.getSettings().interactWithObjectTime);
        watchers.put(WatchersID.INTERACT_WITH_OTHER_OBJECT, interactionWatcher);

        final LogicTimer stopPLayerWatcher = new LogicTimer<>(player.playerData::setStandOnThePlatform, true, .7f);// FIXME: magic number

        stopPLayerWatcher.setLooped(false);
        stopPLayerWatcher.setStopped(true);
        watchers.put(WatchersID.STOP_PLAYER_ON_PLATFORM, stopPLayerWatcher);

        final LogicTimer decreaseLiveWatcher = new LogicTimer<>(player::decreaseLive, 1, 1f);
        watchers.put(WatchersID.DECREASE_LIVE, decreaseLiveWatcher);
        return this;
    }


}
