package com.astro.core.engine.stage.hud;

enum HudElement {
    POINTS_LABEL("", -0.8f, 0.8f),
    HELMET("a", -2.5f, 0.5f),
    LIVE_BAR("c", 0, 0),
    LIVE_BAR_BACKGROUND("b", -1.35f, 0.5f),
    FLY_BAR_BACKGROUND("fly-power", -1.4f, -0.5f),
    FLY_BAR("c", -0.9f, -0.5f);

    final String textureName;

    final float offsetX;

    final float offsetY;

    HudElement(final String textureName, final float offsetX, final float offsetY) {
        this.textureName = textureName;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
