package com.zt.pixtok.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.zt.pixtok.Pixtok;

public class Monster extends Sprite {

    public World world;

    public Body b2body;

    private TextureRegion monsterStand;

    private boolean runningRight;


    public Monster(World world){

        this.world = world;
        runningRight = true;

        initMonster();

        monsterStand = new TextureRegion(new Texture(Gdx.files.internal("monster.png")), 0, 0,  22, 28);
        setBounds(getOriginX(), getOriginY(), (22/ Pixtok.PPM), (28/Pixtok.PPM));
        setRegion(monsterStand);
    }

    private void initMonster(){

        BodyDef bdef = new BodyDef();
        bdef.position.set(((Pixtok.V_WIDTH / 2) / Pixtok.PPM) - 1 , ((Pixtok.V_HEIGHT / 2) / Pixtok.PPM)) ;
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(3.4f/Pixtok.PPM, 15/Pixtok.PPM/2);
        fdef.shape = shape;
        b2body.createFixture(fdef);
        b2body.createFixture(fdef).setUserData("monster");

    }

    public void update(float dt, Pix player){
        setPosition(b2body.getPosition().x - (getWidth()/1.6f), b2body.getPosition().y - (getHeight()/4));
        setRegion(getFrame(monsterStand, player));
    }

    private TextureRegion getFrame(TextureRegion mns, Pix player){

        if((b2body.getPosition().x < player.b2body.getPosition().x) && mns.isFlipX()){
            mns.flip(true, false);
            runningRight = true;
        } else if((b2body.getPosition().x > player.b2body.getPosition().x) && !mns.isFlipX()){
            mns.flip(true, false);
            runningRight = false;
        }

        return mns;
    }

    public boolean isRunningRight(){return runningRight;}

    public void dispose(){

        world.dispose();
    }
}
