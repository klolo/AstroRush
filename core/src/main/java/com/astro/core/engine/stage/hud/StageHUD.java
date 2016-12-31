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
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.astro.core.engine.stage.hud.HudElements.*;

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

    private HashMap<HudElements, GameObject> hudElements = new LinkedHashMap<>();

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
                    .getTextureRegion(HudElements.LIVE_BAR.textureName)
                    .getRegionWidth();
        }
    }

    private void createHudElements() {
        Arrays.stream(HudElements.values())
                .forEach(this::createHudElement);
        hudElements.put(POINTS_LABEL, labelObject);
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

    private void createHudElement(final HudElements hudElement) {
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
        setHelmetWidth(cam);

        setObjectPositionOnScreen(HELMET, cam);
        setObjectPositionOnScreen(POINTS_LABEL, cam);
        setObjectPositionOnScreen(LIVE_BAR_BACKGROUND, cam);

        setObjectPositionOnScreen(hudElements.get(FLY_BAR_BACKGROUND), cam, FLY_BAR_BACKGROUND.offsetX,
                getViewHeight(cam) * 2 + FLY_BAR_BACKGROUND.offsetY);

        setObjectPositionOnScreen(hudElements.get(FLY_BAR), cam, FLY_BAR.offsetX,
                getViewHeight(cam) * 2 + FLY_BAR.offsetY);

        hudElements.get(LIVE_BAR_BACKGROUND).show(cam, delta);
        hudElements.get(HudElements.LIVE_BAR).show(cam, delta);
        hudElements.get(POINTS_LABEL).show(cam, delta);
        hudElements.get(HELMET).show(cam, delta);
        hudElements.get(FLY_BAR_BACKGROUND).show(cam, delta);
        hudElements.get(FLY_BAR).show(cam, delta);

        labelObject.setText(String.valueOf(player.getPoints()));
    }

    private void setHelmetWidth(final OrthographicCamera cam) {
        final TextureRegion region = gameResources
                .getResourceManager()
                .getTextureRegion(LIVE_BAR.textureName);

        final float liveBarWidth = lifeBarStartWidth * (float) player.playerData.getLiveAmount()
                / (float) player.playerData.getStartLiveAmount();
        region.setRegionWidth((int) liveBarWidth);

        float x = getViewWith(cam) - 2.6f;
        final float y = getViewHeight(cam) - 0.51f;

        x += liveBarWidth / pixelPerMeter / 2;

        final Sprite s = new Sprite(region);

        s.setScale(1.0f, 1.0f);
        s.setOrigin(0, 0);
        s.setBounds(x, y, liveBarWidth, region.getRegionHeight());

        hudElements.get(HudElements.LIVE_BAR).getData().setSprite(s);
    }

    private void setObjectPositionOnScreen(final HudElements elements, final OrthographicCamera cam) {
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
