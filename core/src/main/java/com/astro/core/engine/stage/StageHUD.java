package com.astro.core.engine.stage;

import com.astro.core.adnotation.Msg;
import com.astro.core.script.Player;
import com.astro.core.storage.GameResources;
import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.LabelObject;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.extern.slf4j.Slf4j;

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

    @Msg("menu.start")
    private String msg = "";


    private float MARGIN = 0.1f;

    private GameObject playerObject;

    public StageHUD() {
        PropertyInjector.instance.inject(this);

        log.info("Creating default font");

        labelObject = new LabelObject(
                GameResources.instance.getResourceManager().getBitmapFont(
                        LabelObject.getDEFAULT_FONT(),
                        LabelObject.getDEFAULT_SIZE()
                )
        );

        labelObject.setScreenPositionRelative(true);
        playerObject = (GameObject) ObjectsRegister.instance.getObjectByID(Player.IDENTIFIER);
    }

    public void show(final OrthographicCamera cam, float delta) {
        labelObject.getData().getSprite().setPosition(
                -1 * cam.viewportWidth / 2 / 80 + MARGIN,
                cam.viewportHeight / 2 / 80 - MARGIN
        );

        labelObject.setText(msg + playerObject.getData().getSprite().getX());
        labelObject.show(cam, delta);
    }

}
