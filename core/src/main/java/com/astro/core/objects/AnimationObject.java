package com.astro.core.objects;

        import com.astro.core.engine.PhysicsWorld;
        import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.g2d.Animation;
        import com.badlogic.gdx.graphics.g2d.TextureAtlas;
        import lombok.Getter;
        import lombok.Setter;

/**
 * Created by kamil on 01.05.16.
 */
public class AnimationObject extends TextureObject {

    @Getter
    @Setter
    private Animation animation;

    private float elapsedTime = 0;

    public AnimationObject(int fps, final TextureAtlas atlas) {
        animation = new Animation(1 / fps, atlas.getRegions());
    }

    @Override
    protected void render(OrthographicCamera cam, float delta) {
        elapsedTime += delta;
        batch.draw(
                animation.getKeyFrame(elapsedTime, true),
                sprite.getX() * PhysicsWorld.instance.getPIXEL_PER_METER(),
                sprite.getY() * PhysicsWorld.instance.getPIXEL_PER_METER()
        );
    }
}
