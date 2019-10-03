package com.zt.pixtok.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.zt.pixtok.Screens.PlayScreen;
import com.zt.pixtok.Sprites.Monster;
import com.zt.pixtok.Sprites.Pix;

public class WorldContactListener implements ContactListener {

    private Pix pix;
    private Monster monster;

    public WorldContactListener(Pix pix , Monster monster){
        this.monster = monster;
        this.pix = pix;
    }

    @Override
    public void beginContact(Contact contact) {

        String a = (String)contact.getFixtureA().getUserData();
        String b = (String) contact.getFixtureB().getUserData();

        if(a != null && b != null)
        if((b.equals("Spikes") && a.equals("Character")))
            PlayScreen.ended = true;
        else if(b.equals("Ground") && a.equals("Character"))
        PlayScreen.numberJumps = 0;
        else if(b.equals("Jump") && a.equals("monster"))
            monster.b2body.applyLinearImpulse(new Vector2(0 , 2.0f), monster.b2body.getWorldCenter(), true);
        else if(b.equals("monster") && a.equals("Character"))
            PlayScreen.ended = true;
        else if((b.equals("Win") && a.equals("Character")))
            PlayScreen.win = true;


        Gdx.app.log("Collisione", "Collisione tra " + a + " e " + b);
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("Fine contatto", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
