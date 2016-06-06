package com.astro.core.engine.stage;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.LabelObject;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.TextureObject;
import com.astro.core.script.Player;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * heads-up display fo GameStage.
 */
@Slf4j
public class StageHUD implements IGameHud {

    /**
     * Amount of the pixel per meter.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    private LabelObject labelObject;

    private float MARGIN = .8f;

    private GameObject playerObject;

    private Player player;

    private HashMap<HudElements, GameObject> hudElements = new LinkedHashMap<>();

    private static float lifeBarStartWidth = 0.0f;

    enum HudElements {
        POINTS_LABEL,
        HELMET,
        LIVE_BAR,
        LIVE_BAR_BACKGROUND
    }

    StageHUD() {
        PropertyInjector.instance.inject(this);

        log.info("Creating default font");
        labelObject = new LabelObject(
                GameResources.instance.getResourceManager().getBitmapFont(
                        LabelObject.getDEFAULT_FONT(),
                        LabelObject.getDEFAULT_SIZE()
                )
        );

        labelObject.setScreenPositionRelative(true);
        hudElements.put(HudElements.POINTS_LABEL, labelObject);

        initPlayerData();

        TextureObject liveBarBackground = createTextureObject("b");
        hudElements.put(HudElements.LIVE_BAR_BACKGROUND, liveBarBackground);

        TextureObject liveBar = createTextureObject("c");
        hudElements.put(HudElements.LIVE_BAR, liveBar);

        if (lifeBarStartWidth == 0.0f) {
            lifeBarStartWidth = GameResources.instance.getResourceManager().getTextureRegion("c").getRegionWidth();
        }

        final TextureObject helmet = createTextureObject("a");
        hudElements.put(HudElements.HELMET, helmet);
    }


    private TextureObject createTextureObject(final String name) {
        TextureObject result = new TextureObject(GameResources.instance.getResourceManager().getTextureRegion(name));
        TextureRegion region = result.getTextureRegion();

        result.getData().getSprite().setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
        result.getData().getSprite().setScale(1.0f, 1.0f);
        result.getData().getSprite().setOrigin(0f, 0f);
        result.setScreenPositionRelative(true);

        return result;
    }


    /**
     * Getting player data for future usage.
     */
    private void initPlayerData() {
        playerObject = (GameObject) ObjectsRegister.instance.getObjectByID(Player.IDENTIFIER);
        player = (Player) playerObject.getData().getLogic();
    }

    public void show(final OrthographicCamera cam, float delta) {
        setHelmetWidth(cam);

        setObjectPositionOnScreen(hudElements.get(HudElements.HELMET), cam, -2.5f, .5f);
        setObjectPositionOnScreen(hudElements.get(HudElements.POINTS_LABEL), cam, -1 * MARGIN, MARGIN);
        setObjectPositionOnScreen(hudElements.get(HudElements.LIVE_BAR_BACKGROUND), cam, -1.35f, .5f);

        labelObject.setText(String.valueOf(player.getPoints()));
        hudElements.values().forEach(e -> e.show(cam, delta));
    }

    private void setHelmetWidth(final OrthographicCamera cam) {
        TextureRegion region = GameResources.instance.getResourceManager().getTextureRegion("c");

        final float liveBarWidth = lifeBarStartWidth * (float) player.getLiveAmount() / (float) player.getStartLiveAmount();
        region.setRegionWidth((int) liveBarWidth);

        float x = getViewWith(cam) - 2.6f;
        float y = getViewHeight(cam) - 0.51f;

        x += liveBarWidth / PIXEL_PER_METER / 2;

        final Sprite s = new Sprite(region);

        s.setScale(1.0f, 1.0f);
        s.setOrigin(0, 0);
        s.setBounds(x, y, liveBarWidth, region.getRegionHeight());

        hudElements.get(HudElements.LIVE_BAR).getData().setSprite(s);
    }

    private void setObjectPositionOnScreen(final GameObject object, final OrthographicCamera cam, float offsetX, float offsetY) {
        float x = getViewWith(cam) + offsetX;
        float y = getViewHeight(cam) - offsetY;

        object.getData().getSprite().setBounds(
                x,
                y,
                object.getData().getSprite().getRegionWidth(),
                object.getData().getSprite().getRegionHeight()
        );
    }

    private float getViewWith(final OrthographicCamera cam) {
        return cam.viewportWidth / 2 / PIXEL_PER_METER;
    }

    private float getViewHeight(final OrthographicCamera cam) {
        return cam.viewportHeight / 2 / PIXEL_PER_METER;
    }
}
