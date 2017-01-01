package com.astro.core.logic.player;

import com.astro.core.engine.base.CameraManager;
import com.astro.core.logic.common.LogicTimer;
import com.astro.core.logic.common.PopupMsg;
import com.astro.core.logic.player.fire.IFireBehavior;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Scope("prototype")
@Getter
@Setter
public class PlayerData {

    @Autowired
    public PopupMsg playerPopupMsg;

    @Autowired
    public PlayerSettings settings;

    @Autowired
    public CameraManager cameraManager;

    @Autowired
    public IFireBehavior fireBehavior;

    public IInteractWithPlayer interactObject;

    public boolean standOnThePlatform;

    public boolean canShoot = true;

    public final static int HORIZONTAL_FORCE_STRENGHT = 5;

    public int points;

    public byte liveAmount = 100;
    public final byte startLiveAmount = 100;

    public float flyPowerAmount = 100;
    public final float startFlyPowerAmount = 100;

    public float posX;

    public float posY;

    public boolean isDead;

    public PlayerState state = PlayerState.STAND;

    public PlayerCollisionProcessor playerCollisionProcessor;

    public HashMap<WatchersID, LogicTimer> watchers;

    public IGameObject gameObject;

    public LogicTimer shootTimer;

    public Body physicBody;

    /**
     * Hold default Player graphic (animation of run) and additional graphics like fly and stand.
     */
    public PlayerGraphics graphics;
}
