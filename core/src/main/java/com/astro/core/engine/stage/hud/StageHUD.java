package com.astro.core.engine.stage.hud;

import com.astro.core.logic.Player;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.LabelObject;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.TextureObject;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.astro.core.engine.stage.hud.HudElement.*;


@Slf4j
@Component
@Scope("prototype")
public class StageHUD implements IGameHud, ApplicationContextAware {

    @Autowired
    private HudProgressBarFactory hudProgressBarFactory;

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

    private HudProgressBar flyProgressBar;

    private float lifeBarStartWidth;

    private HudElement[] elementInOrders = {LIVE_BAR_BACKGROUND, LIVE_BAR, POINTS_LABEL, HELMET, FLY_BAR_BACKGROUND, FLY_BAR};

    @Override
    public void init() {
        LOGGER.info("Creating HUD");
        initPointsLabel();
        initPlayerData();
        setLifeBarStartWidth();
        createHudElements();
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

    private void setLifeBarStartWidth() {
        if (lifeBarStartWidth == 0.0f) {
            lifeBarStartWidth = gameResources
                    .getResourceManager()
                    .getTextureRegion(HudElement.LIVE_BAR.textureName)
                    .getRegionWidth();
        }
    }

    private void createHudElements() {
        Arrays.stream(HudElement.values()).forEach(this::createHudElement);
        hudElements.put(POINTS_LABEL, labelObject);
        flyProgressBar = hudProgressBarFactory.createFromHudElement(LIVE_BAR, Location.BOTTOM_RIGHT);
        liveProgressBar = hudProgressBarFactory.createFromHudElement(LIVE_BAR, Location.TOP_RIGHT);
    }

    private void createHudElement(final HudElement hudElement) {
        if (hudElement.textureName.equals("")) {
            return;
        }

        final TextureObject elementTexture = createTextureObject(hudElement.textureName);
        hudElements.put(hudElement, elementTexture);
    }

    private TextureObject createTextureObject(final String name) {
        final TextureObject result = applicationContext.getBean("textureObject", TextureObject.class);
        final TextureRegion region = gameResources.getResourceManager().getTextureRegion(name);

        result.setTextureRegion(region);
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
        scaleProgressBars(cam);

        setObjectPositionOnScreen(HELMET, cam);
        setObjectPositionOnScreen(POINTS_LABEL, cam);
        setObjectPositionOnScreen(LIVE_BAR_BACKGROUND, cam);
        setObjectPositionOnScreen(FLY_BAR_BACKGROUND, cam, FLY_BAR_BACKGROUND.offsetX, getViewHeight(cam) * 2 + FLY_BAR_BACKGROUND.offsetY);

        Arrays.stream(elementInOrders)
                .forEach(element -> hudElements.get(element).show(cam, delta));

        labelObject.setText(String.valueOf(player.getPoints()));
    }

    private void scaleProgressBars(final OrthographicCamera cam) {
        final Sprite scaledSpriteLiveProgressBarSprite = liveProgressBar.getScaledSprite(cam,
                player.playerData.getLiveAmount(),
                player.playerData.getStartLiveAmount()
        );
        hudElements.get(LIVE_BAR).getData().setSprite(scaledSpriteLiveProgressBarSprite);

        final Sprite scaledFlyProgressBarSprite = flyProgressBar.getScaledSprite(
                cam,
                player.playerData.getFlyPowerAmount(),
                player.playerData.getStartFlyPowerAmount()
        );
        hudElements.get(FLY_BAR).getData().setSprite(scaledFlyProgressBarSprite);
    }

    private void setObjectPositionOnScreen(final HudElement elements, final OrthographicCamera cam, final float offsetX, final float offsetY) {
        setObjectPositionOnScreen(hudElements.get(elements), cam, offsetX, offsetY);
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
