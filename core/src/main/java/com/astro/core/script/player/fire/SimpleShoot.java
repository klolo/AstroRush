package com.astro.core.script.player.fire;

import com.astro.core.objects.GameObject;
import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleShoot implements IFireBehavior {

    private PhysicsObject physicsObject;

    public GameObject onFire(final float positionX, final float positionY) {
        physicsObject = new PhysicsObject(GameResources.instance.getResourceManager().getTextureRegion("keyRed"));

        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = positionX;
        bodyDef.position.y = positionY;

        final PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(.1f, .1f);

        final FixtureDef myFixtureDef = new FixtureDef();
        myFixtureDef.shape = polygonShape;
        myFixtureDef.density = 1;

        physicsObject.setBodyDef(bodyDef);
        physicsObject.setFixtureDef(myFixtureDef);

        physicsObject.init();
        physicsObject.getData().setLogic(this);
        return physicsObject;
    }

    @Override
    public void update(final float diff) {
        physicsObject.updatePosition();
    }

    @Override
    public void setGameObject(final IGameObject gameObject) {

    }

}
