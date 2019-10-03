package com.zt.pixtok.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zt.pixtok.Pixtok;
import com.zt.pixtok.Tools.Values;

import static com.badlogic.gdx.graphics.Color.WHITE;

public class Hud {

    public Stage stage;

    private Viewport viewport;

    private Integer worldTimer;
    private Integer score;

    private Table table;

    private ImageButton volume;

    private Drawable vol_on, vol_off;

    private Music music;

    private Label countTimeLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label pixLabel;

    public Hud (SpriteBatch sb) {

        vol_on = new TextureRegionDrawable(new TextureRegion(new Texture("volume/volume_on.png")));
        vol_off = new TextureRegionDrawable(new TextureRegion(new Texture("volume/volume_off.png")));
        music = Pixtok.manager.get("music/level_music.ogg", Music.class);
        music.setVolume(0.35f);
        music.setLooping(true);

        if(Values.volumeOn) {
            volume = new ImageButton(vol_on);
            music.play();
        } else{
            volume = new ImageButton(vol_off);
        }

        addListener();

        worldTimer = 0;
        score = 0;

        viewport = new FitViewport(Pixtok.V_WIDTH, Pixtok.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);


        countTimeLabel = new Label(String.format("%06d", worldTimer), new Label.LabelStyle(new BitmapFont(), WHITE) );
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), WHITE) );
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), WHITE) );
        worldLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), WHITE) );
        pixLabel = new Label("PIX", new Label.LabelStyle(new BitmapFont(), WHITE) );
        levelLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), WHITE) );

        table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(pixLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(volume).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countTimeLabel).expandX();
        table.row();


        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);


    }

        public void setTime(String time){

            countTimeLabel.setText(time);
        }

        public void addListener(){

            volume.addListener(new ClickListener(){

                @Override
                public void clicked(InputEvent event, float x, float y){

                    if(Values.volumeOn){

                        music.pause();
                        table.removeActor(volume);
                        volume = new ImageButton(vol_off);
                        table.add(volume);
                        stage.addActor(table);
                        Values.volumeOn = false;
                    } else {

                        music.play();
                        table.removeActor(volume);
                        volume = new ImageButton(vol_on);
                        table.add(volume);
                        stage.addActor(table);
                        Values.volumeOn = true;
                    }
                    addListener();
                }
            });
        }

        public void stopMusic(){music.stop();}

        public void dispose(){

            music.dispose();
            stage.dispose();
        }

}