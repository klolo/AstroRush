package com.astro.core.script.player;

import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;

/**
 * Created by kamil on 15.05.16.
 */
public class PlayerGraphics {

    /**
     *
     */
    private TextureObject standImage;

    /**
     *
     */
    private TextureObject flyImage;

    /**
     * Animation of the player. Keep position and size for other player graphics.
     */
    @Getter
    private AnimationObject runAnimation;

    /**
     * TODO:
     *
     */
    public IGameObject getTextureBasedOnState(final PlayerState state) {
        switch (state) {
            case STAND: {
                return standImage;
            }
            case FLY_LEFT: {
                return flyImage;
            }
            case FLY_RIGHT: {
                return flyImage;
            }
            default: {
                return runAnimation;
            }
        }
    }


    public PlayerGraphics(AnimationObject animation) {
        runAnimation = animation;

        standImage = createImageBasedOnBaseAnimation("stand");
        flyImage = createImageBasedOnBaseAnimation("fly");
    }

    private TextureObject createImageBasedOnBaseAnimation( final String name ) {
        TextureObject result = new TextureObject(GameResources.instance.getResourceManager().getTextureRegion(name));

        Sprite animSprite = runAnimation.getData().getSprite();
        result.getData().getSprite().setOrigin(animSprite.getOriginX(), animSprite.getOriginY());
        result.getData().getSprite().setScale(animSprite.getScaleX(), animSprite.getScaleY());
        return result;
    }
}
