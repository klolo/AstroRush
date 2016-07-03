package com.astro.core.script.player;

import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.script.player.fire.IFireBehavior;
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

    protected final static int HORIZONTAL_FORCE_STRENGHT = 5;

    protected int points = 0;

    @Getter
    protected int liveAmount = 100;

    @Getter
    protected int startLiveAmount = 100;

    @Getter
    protected PlayerSettings settings = new PlayerSettings();

    protected PlayerState state = PlayerState.STAND;

    @Getter
    protected CollisionProcessor collisionProcessor;

    @Getter
    protected HashMap<WatchersID, LogicTimer> watchers;

    @Setter
    protected IInteractWithPlayer interactObject;

    @Setter
    protected boolean standOnThePlatform = false;

    protected float posX;

    protected float posY;

    @Getter
    protected boolean isDead;

    protected IFireBehavior fireBehavior;

    protected IGameObject gameObject;

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

    public PopupMsg getPlayerPopupMsg() {
        return playerPopupMsg;
    }
}
