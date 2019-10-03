package com.zt.pixtok.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zt.pixtok.Pixtok;
import com.zt.pixtok.Tools.Values;

public class Menu implements Screen {

    private Texture background;
    private Texture imgb;
    private Texture madeBy, name;

    private ImageButton playButton;
    private ImageButton volume;

    private Camera menuCam;

    private Table table;

    private Viewport viewport;

    private Stage stage;

    private Pixtok game;

    private Music music;

    private Drawable vol_on, vol_off;



    public Menu(Pixtok game){

        vol_on = new TextureRegionDrawable(new TextureRegion(new Texture("volume/volume_on.png")));
        vol_off = new TextureRegionDrawable(new TextureRegion(new Texture("volume/volume_off.png")));
        music = Pixtok.manager.get("music/menu_music.ogg", Music.class);
        music.setVolume(0.35f);
        music.setLooping(true);

        if(Values.volumeOn) {
            volume = new ImageButton(vol_on);
            music.play();
        } else{
            volume = new ImageButton(vol_off);
        }

        this.game = game;
        background = new Texture("menu1.jpg");
        imgb = new Texture("PlayButton.png");
        madeBy = new Texture("logo nomi.png");
        name = new Texture("logo menu.png");
        menuCam = new OrthographicCamera();
        Image imageName = new Image(name);
        Image imageMadeBy = new Image(madeBy);

        viewport = new FitViewport(Pixtok.V_WIDTH, Pixtok.V_HEIGHT, menuCam);
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(imgb));
        playButton = new ImageButton(drawable);


        table = new Table();
        table.center();
        table.setFillParent(true);
        table.add();
        table.add(imageName, playButton, imageMadeBy);
        table.add();
        table.add(volume).expandY().bottom().right();
        stage = new Stage(viewport, game.batch);
        stage.addActor(table);

        menuCam.update();
    }




    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        playButton.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y){

                music.stop();
                stage.dispose();
                game.setScreen(new PlayScreen(game));
            }
        });

        volume.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y){

                if(Values.volumeOn){

                    table.removeActor(volume);
                    volume = new ImageButton(vol_off);
                    table.add(volume).expandY().bottom().right();
                    Values.volumeOn = false;
                    music.pause();
                } else {

                    table.removeActor(volume);
                    volume = new ImageButton(vol_on);
                    table.add(volume).expandY().bottom().right();
                    Values.volumeOn = true;
                    music.play();
                }
                show();
            }
        });
    }




    @Override
    public void render(float delta) {



        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0, Pixtok.V_WIDTH, Pixtok.V_HEIGHT);
        //game.batch.draw(name, (name.getWidth()/6), Pixtok.V_HEIGHT/4, Pixtok.V_WIDTH, Pixtok.V_HEIGHT);
        //game.batch.draw(madeBy, (madeBy.getWidth()), -Pixtok.V_HEIGHT/2, Pixtok.V_WIDTH/2, Pixtok.V_HEIGHT/1.04f);
        //playButton.draw(game.batch,0);
        game.batch.end();

        stage.draw();
    }



    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        music.dispose();
        background.dispose();
        imgb.dispose();
        madeBy.dispose();
        name.dispose();
        stage.dispose();
        game.dispose();
    }
}
