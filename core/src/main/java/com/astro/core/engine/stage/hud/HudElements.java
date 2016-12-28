package com.astro.core.engine.stage.hud;

public enum HudElements {
    POINTS_LABEL(""),
    HELMET("a"),
    LIVE_BAR("c"),
    LIVE_BAR_BACKGROUND("b"),
    JUMP_BAR("");

    final String textureName;

    HudElements(final String textureName) {
        this.textureName = textureName;
    }
}
