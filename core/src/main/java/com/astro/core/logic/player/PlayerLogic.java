package com.astro.core.logic.player;

import com.astro.core.engine.base.GameEngine;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PlayerLogic implements ILogic {

    public PlayerData playerData;

    PlayerLogic() {
        LOGGER.info("Creating player");
        playerData = GameEngine.getApplicationContext().getBean(PlayerData.class);
        playerData.initializeDefaultValue();
    }

    /**
     * Set data loaded from json, which contains physics information and base animation.
     */
    public void setGameObject(final IGameObject gameObject) {
        LOGGER.info("Set game object player");
        playerData.playerPopupMsg.initLabel();
        playerData.gameObject = gameObject;

        gameObject.getData().setCollisionCallbackFunction(playerData.playerCollisionProcessor::processCollision);
        setPlayerHeight((AnimationObject) gameObject);

        playerData.physicBody = gameObject.getData().getBody();
        playerData.physicBody.setFixedRotation(true);

        playerData.graphics = new PlayerGraphics(
                (AnimationObject) gameObject,
                GameEngine.getApplicationContext().getBean(GameResources.class));
    }

    private void setPlayerHeight(final AnimationObject gameObject) {
        final Object[] animation = gameObject
                .getAnimation()
                .getKeyFrames();
        playerData.settings.playerHeight = ((TextureRegion) animation[0]).getRegionHeight() / playerData.settings.pixelPerMeter;
    }

    @Override
    public void additionalRender(final OrthographicCamera cam, final float delta) {
        playerData.playerPopupMsg.show(cam, delta);

        if (playerData.graphics.getRunAnimation().isRenderingInScript()) {
            renderPlayer(cam, delta);
        }
    }

    private void renderPlayer(final OrthographicCamera cam, final float delta) {
        final IGameObject gfxObject = playerData.graphics.getTextureBasedOnState(playerData.state);
        gfxObject.getData().getSprite().setPosition(playerData.posX, playerData.posY);

        if (playerData.state.isFly()) {
            (gfxObject).getData().setFlipX(playerData.state == PlayerState.FLY_LEFT);
        }

        gfxObject.show(cam, delta);
    }

    @Override
    public void update(final float diff) {
        updatePosition();
        playerData.playerPopupMsg.update(diff);
        playerData.watchers.values().stream()
                .filter(o -> o != null)
                .forEach(w -> w.update(diff));
        playerData.fireBehavior.update(playerData.posX, playerData.posY);
    }

    private void updatePosition() {
        playerData.posX = playerData.graphics.getRunAnimation().getData().getSprite().getX();
        playerData.posY = playerData.graphics.getRunAnimation().getData().getSprite().getY();

        final float newX = playerData.physicBody.getPosition().x;
        final float newY = playerData.physicBody.getPosition().y;

        playerData.state = playerData.state.getState(playerData.posX, playerData.posY, newX, newY, playerData.state);
        if (playerData.standOnThePlatform) {
            playerData.state = PlayerState.STAND;
        }
        playerData.graphics.getRunAnimation().setRenderingInScript(!playerData.state.isRun());

        if (playerData.physicBody.getPosition().y > playerData.settings.maxYPosition) {
            playerData.physicBody.setTransform(newX, playerData.settings.maxYPosition, 0);
        }
        playerData.graphics.getRunAnimation().getData().getSprite().setPosition(newX, newY);
        playerData.playerPopupMsg.setPosWithCenter(newX, newY + 2 * playerData.settings.playerHeight);
    }

}
