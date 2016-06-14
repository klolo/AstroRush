package com.astro.core.script.player.fire;

import com.astro.core.objects.GameObject;
import com.astro.core.objects.PhysicsObject;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;


public class SimpleShoot implements IFireBehavior {

    public GameObject onFire() {
        final PhysicsObject result = new PhysicsObject(GameResources.instance.getResourceManager().getTextureRegion("keyRed"));

        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.x = 0;
        bodyDef.position.y = 0;

        result.setBodyDef(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(.1f, .1f);

        FixtureDef myFixtureDef = new FixtureDef();
        myFixtureDef.shape = polygonShape;
        myFixtureDef.density = 1;

        result.setFixtureDef(myFixtureDef);
        result.init();
        return result;
    }

}
