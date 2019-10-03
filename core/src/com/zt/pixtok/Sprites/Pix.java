package com.zt.pixtok.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.zt.pixtok.Pixtok;
import com.zt.pixtok.Screens.PlayScreen;


public class Pix extends Sprite {

    public enum State {RUNNING, FALLING, JUMPING, STANDING, MID_AIR};

    public State currentState;
    public State previousState;

    public World world;

    public Body b2body;


    private Animation pixRun;

    private boolean runningRight;
    private float stateTimer;
    private TextureRegion pixStand;

    public Pix(World world, PlayScreen screen){

        super(screen.getAtlasRun().findRegion("running_0"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;



        pixStand = new TextureRegion(getTexture(), 1,1, 50, 37);

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(getTexture(), 1, 196, 50, 37));
        frames.add(new TextureRegion(getTexture(), 1, 157, 50, 37));
        frames.add(new TextureRegion(getTexture(), 1, 118, 50, 37));
        frames.add(new TextureRegion(getTexture(), 1, 79, 50, 37));
        frames.add(new TextureRegion(getTexture(), 1, 40, 50, 37));
        frames.add(new TextureRegion(getTexture(), 1, 1, 50, 37));

        initPix();
        setBounds(getOriginX(),getOriginY() + 34, (50/Pixtok.PPM), (37/Pixtok.PPM));
        setRegion(pixStand);




        pixRun = new Animation(0.1f, frames);






    }

    public void initPix(){

        BodyDef bdef = new BodyDef();
        bdef.position.set(((Pixtok.V_WIDTH / 2) / Pixtok.PPM) , ((Pixtok.V_HEIGHT / 2) / Pixtok.PPM)) ;
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        //CircleShape shape = new CircleShape();
       // shape.setRadius(5/Pixtok.PPM);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2/Pixtok.PPM, 8/Pixtok.PPM/2);



        fdef.shape = shape;
        b2body.createFixture(fdef);

        b2body.createFixture(fdef).setUserData("Character");


    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - (getWidth()/1.6f), b2body.getPosition().y - (getHeight()/4));
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){

        currentState = getState();

        TextureRegion region = pixStand;

        switch(currentState){

            case RUNNING:
                region = (TextureRegion) pixRun.getKeyFrame(0, true);
                    break;

            case FALLING:
                break;
            case JUMPING: break;

            case STANDING:
                break;
            case MID_AIR:
                break;
            default: region = pixStand;
                break;

        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){

            region.flip(true, false);
            runningRight = false;
        } else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){

            region.flip(true, false);
            runningRight = true;
        }

        previousState = currentState;

        return region;
    }

    public State getState(){

        if(b2body.getLinearVelocity().y > 0)
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void dispose(){

        world.dispose();
    }

}
