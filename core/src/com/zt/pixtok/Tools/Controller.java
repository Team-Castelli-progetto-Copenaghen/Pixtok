

package com.zt.pixtok.Tools;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zt.pixtok.Pixtok;
import com.zt.pixtok.Screens.PlayScreen;

public class Controller {

    public Stage stage;

    private Viewport viewport;

    private ImageButton left, right, up;

    private Table table;

    private boolean leftPressed, rightPressed;
    private boolean upPressed;

    public Controller(Pixtok game){

        leftPressed = false;
        rightPressed = false;
        upPressed = false;

        left = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("arrows/flatDark23.png")))));
        right = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("arrows/flatDark24.png")))));
        up = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("arrows/flatDark25.png")))));

        addListener();



        viewport = new FitViewport(Pixtok.V_WIDTH, Pixtok.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        table = new Table();
        table.bottom();

        table.setFillParent(true);
        table.add();
        table.add(left).expandX().left();
        table.add(right).expandX().left();
        table.add(up).expandX().right();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private void addListener(){

        left.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                leftPressed = true;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                leftPressed = false;
            }
        });


        right.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                rightPressed = true;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                rightPressed = false;
            }
        });

        up.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(PlayScreen.numberJumps == 2) upPressed = false;
                else upPressed = true;
            }
        });

    }

    public boolean isLeftPressed(){
        return leftPressed;
    }
    public boolean isRightPressed(){
        return rightPressed;
    }
    public boolean getUpPressed(){
        return upPressed;
    }
    public void setUpPressed(boolean b){
        upPressed = b;
    }

    public void dispose(){

        stage.dispose();
    }

}
