package com.astro.core.engine.stage;

import com.astro.core.engine.GameResources;
import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.LabelObject;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.storage.PropertyInjector;
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

    public StageHUD() {
        PropertyInjector.instance.inject(this);

        log.info("Creating default font");

        labelObject = new LabelObject(
                GameResources.instance.getResourceManager().getBitmapFont(LabelObject.getDEFAULT_FONT(),
                        LabelObject.getDEFAULT_SIZE()));


        labelObject.getSprite().setPosition(
                -1 * (Gdx.graphics.getWidth() / (2 * PIXEL_PER_METER)) + 1.0f, //fixme
                -1 * (Gdx.graphics.getHeight() / (2 * PIXEL_PER_METER)) + 10.0f //fixme
        );
        labelObject.setScreenPositionRelative(true);
    }


    public void show(final OrthographicCamera cam, float delta) {
        GameObject playerObject = (GameObject) ObjectsRegister.instance.getObjectByID("player");// fixme: not call in loop
        labelObject.setText("Pos X:" + playerObject.getSprite().getX());
        labelObject.show(cam, delta);
    }

}
