package com.astro.core.script.player;

import com.astro.core.script.util.LogicTimer;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * All player non final field.
 */
public class PlayerData {

    protected PopupMsg playerPopupMsg = new PopupMsg();

    protected int points = 0;

    @Getter
    protected int liveAmount = 100;

    @Getter
    protected int startLiveAmount = 100;

    @Getter
    protected PlayerSettings settings = new PlayerSettings();

    protected PlayerState state = PlayerState.STAND;

    protected PlayerCollisionProcesor collisionProcesor;

    protected HashMap<WatchersID, LogicTimer> watchers;

    @Setter
    protected IInteractWithPlayer interactObject;

    @Setter
    protected boolean standOnThePlatform = false;

    protected float posX = 0.0f;

    protected float posY = 0.0f;

    /**
     * Physics body.
     */
    @Getter
    protected Body body;


    /**
     * Hold default Player graphic (animation of run) and additional
     * graphics like fly and stand.
     */
    protected PlayerGraphics graphics;

    public PopupMsg getPlayerPopupMsg(){
        return playerPopupMsg;
    }
}
