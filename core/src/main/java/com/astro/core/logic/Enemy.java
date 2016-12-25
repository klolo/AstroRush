package com.astro.core.logic;

import com.astro.core.engine.base.GameEngine;
import com.astro.core.engine.physics.CollisionProcessResult;
import com.astro.core.logic.common.BackAndForthMove;
import com.astro.core.logic.common.PopupMsg;
import com.astro.core.logic.player.fire.IShootLogic;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.extern.slf4j.Slf4j;

/**
 * Test logic for sheep.
 */
@Slf4j
public class Enemy implements ILogic {

    private AnimationObject gameObject;

    private BackAndForthMove move;

    public PopupMsg popupMsg;

    float liveAmount = 1.0f;

    public Enemy() {
        popupMsg = GameEngine.getApplicationContext().getBean(PopupMsg.class);
        popupMsg.setCurrentMsg("Kill me!");
        popupMsg.getMessagesQueue().clear();
        popupMsg.setInstantAdd(true);
    }

    public void setGameObject(final IGameObject gameObject) {
        this.gameObject = (AnimationObject) gameObject;
        this.gameObject.getData().setCollisionCallbackFunction(this::collisionEvent);
        popupMsg.initLabel();
        ((AnimationObject) gameObject).setRenderingInScript(true);
        move = new BackAndForthMove<>((AnimationObject) gameObject, -2f, 2f, 1f);
    }

    CollisionProcessResult collisionEvent(final IGameObject gameObject) {
        if (gameObject.getData().getLogic() instanceof IShootLogic) {
            LOGGER.debug("[Enemy] hit");
            final IShootLogic shootLogic = (IShootLogic) gameObject.getData().getLogic();
            liveAmount -= shootLogic.getDamageAmount();
            popupMsg.addMessagesToQueue("Live: " + (int) (liveAmount * 100) + " %");
        }

        return new CollisionProcessResult();
    }

    @Override
    public void update(final float diff) {
        if (liveAmount <= 0f) {
            LOGGER.debug("[Enemy] dead");
            gameObject.getData().setDestroyed(true);
            final Player player = (Player) GameEngine.getApplicationContext().getBean(ObjectsRegister.class)
                    .getObjectByID(Player.IDENTIFIER).getData().getLogic();
            player.addPointsWithMessages(100);//fixme
            return;
        }

        move.update(diff);
        popupMsg.setPosWithCenter(gameObject.getData().getSprite().getX(), gameObject.getData().getSprite().getY() + 2);
        popupMsg.update(diff);
    }

    @Override
    public void additionalRender(final OrthographicCamera cam, final float delta) {
        gameObject.showFromLogic(cam, delta);
        popupMsg.show(cam, delta);
    }
}
