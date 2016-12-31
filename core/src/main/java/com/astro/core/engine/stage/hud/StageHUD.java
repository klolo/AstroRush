package com.astro.core.engine.stage.hud;

import com.astro.core.logic.Player;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.LabelObject;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.TextureObject;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.astro.core.engine.stage.hud.HudElement.*;

/**
 * heads-up display fo GameStage.
 */
@Slf4j
@Component
public class StageHUD implements IGameHud, ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Value("${renderer.pixel.per.meter}")
    protected short pixelPerMeter;

    @Autowired
    private ObjectsRegister objectsRegister;

    @Autowired
    private GameResources gameResources;

    private LabelObject labelObject;

    private GameObject playerObject;

    private Player player;

    private HashMap<HudElement, GameObject> hudElements = new LinkedHashMap<>();

    private HudProgressBar liveProgressBar;

    private static float lifeBarStartWidth;

    @Override
    public void init() {
        LOGGER.info("Creating default font");
        initPointsLabel();
        initPlayerData();
        setLifeBarStartWidth();
        createHudElements();
    }

    private void setLifeBarStartWidth() {
        if (lifeBarStartWidth == 0.0f) {
            lifeBarStartWidth = gameResources
                    .getResourceManager()
                    .getTextureRegion(HudElement.LIVE_BAR.textureName)
                    .getRegionWidth();
        }
    }

    private void createHudElements() {
        Arrays.stream(HudElement.values())
                .forEach(this::createHudElement);
        hudElements.put(POINTS_LABEL, labelObject);

        liveProgressBar = new HudProgressBar(gameResources
                .getResourceManager()
                .getTextureRegion(LIVE_BAR.textureName), pixelPerMeter);
    }

    private void initPointsLabel() {
        labelObject = applicationContext.getBean(LabelObject.class);
        labelObject.setBitmapFont(
                gameResources.getResourceManager().getBitmapFont(
                        LabelObject.getDEFAULT_FONT(),
                        LabelObject.getDEFAULT_SIZE()
                ));

        labelObject.setScreenPositionRelative(true);
    }

    private void createHudElement(final HudElement hudElement) {
        if (!hudElement.textureName.equals("")) {
            final TextureObject elementTexture = createTextureObject(hudElement.textureName);
            hudElements.put(hudElement, elementTexture);
        }
    }

    private TextureObject createTextureObject(final String name) {
        final TextureObject result = applicationContext.getBean("textureObject", TextureObject.class);
        result.setTextureRegion(gameResources.getResourceManager().getTextureRegion(name));

        final TextureRegion region = result.getTextureRegion();

        result.getData().getSprite().setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
        result.getData().getSprite().setScale(1.0f, 1.0f);
        result.getData().getSprite().setOrigin(0f, 0f);
        result.setScreenPositionRelative(true);

        return result;
    }

    private void initPlayerData() {
        playerObject = (GameObject) objectsRegister.getObjectByID(Player.IDENTIFIER);
        player = (Player) playerObject.getData().getLogic();
    }

    public void show(final OrthographicCamera cam, final float delta) {
        //setElementDynamicWidth(cam, LIVE_BAR, player.playerData.getLiveAmount(), player.playerData.getStartLiveAmount(), lifeBarStartWidth);
        //setElementDynamicWidth(cam, FLY_BAR, player.playerData.getLiveAmount(), player.playerData.getStartLiveAmount(), lifeBarStartWidth);

        hudElements.get(LIVE_BAR).getData().setSprite(
                liveProgressBar.getScaledSprite(cam,
                        player.playerData.getLiveAmount(), player.playerData.getStartLiveAmount(), lifeBarStartWidth));


        setObjectPositionOnScreen(HELMET, cam);
        setObjectPositionOnScreen(POINTS_LABEL, cam);
        setObjectPositionOnScreen(LIVE_BAR_BACKGROUND, cam);

        setObjectPositionOnScreen(hudElements.get(FLY_BAR_BACKGROUND), cam, FLY_BAR_BACKGROUND.offsetX,
                getViewHeight(cam) * 2 + FLY_BAR_BACKGROUND.offsetY);

        setObjectPositionOnScreen(hudElements.get(FLY_BAR), cam, FLY_BAR.offsetX,
                getViewHeight(cam) * 2 + FLY_BAR.offsetY);

        hudElements.get(LIVE_BAR_BACKGROUND).show(cam, delta);
        hudElements.get(HudElement.LIVE_BAR).show(cam, delta);
        hudElements.get(POINTS_LABEL).show(cam, delta);
        hudElements.get(HELMET).show(cam, delta);
        hudElements.get(FLY_BAR_BACKGROUND).show(cam, delta);
        hudElements.get(FLY_BAR).show(cam, delta);

        labelObject.setText(String.valueOf(player.getPoints()));
    }

    private void setObjectPositionOnScreen(final HudElement elements, final OrthographicCamera cam) {
        setObjectPositionOnScreen(hudElements.get(elements), cam, elements.offsetX, elements.offsetY);
    }

    private void setObjectPositionOnScreen(final GameObject object, final OrthographicCamera cam,
                                           final float offsetX, final float offsetY) {
        final float x = getViewWith(cam) + offsetX;
        final float y = getViewHeight(cam) - offsetY;

        object.getData().getSprite().setBounds(
                x,
                y,
                object.getData().getSprite().getRegionWidth(),
                object.getData().getSprite().getRegionHeight()
        );
    }

    private float getViewWith(final OrthographicCamera cam) {
        return cam.viewportWidth / 2 / pixelPerMeter;
    }

    private float getViewHeight(final OrthographicCamera cam) {
        return cam.viewportHeight / 2 / pixelPerMeter;
    }
}
