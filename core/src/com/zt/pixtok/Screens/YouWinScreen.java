package com.zt.pixtok.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zt.pixtok.Pixtok;
import com.zt.pixtok.Tools.Values;


public class YouWinScreen implements Screen {

    private Texture gameWin;

    private ImageButton mainMenu;
    private ImageButton playAgain;

    private Camera gameOverCamera;

    private Viewport viewport;

    private Stage stage;

    private Pixtok game;

    private Music music;

    public YouWinScreen(Pixtok game){

        music = Pixtok.manager.get("music/victory.ogg", Music.class);
        music.setVolume(0.5f);
        music.setLooping(true);
        if(Values.volumeOn)
        music.play();

        this.game = game;
        gameWin = new Texture(Gdx.files.internal("you_win.jpg"));

        playAgain = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("replay.png")))));
        mainMenu = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("main_menu.png")))));
        gameOverCamera = new OrthographicCamera();
        viewport = new FitViewport(Pixtok.V_WIDTH, Pixtok.V_HEIGHT, gameOverCamera);
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add();
        table.add(playAgain).expand().bottom().left();
        table.add(mainMenu).expandY().bottom().right();
        stage = new Stage(viewport, game.batch);
        stage.addActor(table);

        gameOverCamera.update();
    }


    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        playAgain.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y){

                if(music.isPlaying())
                music.stop();
                game.setScreen(new PlayScreen(game));
            }
        });
        mainMenu.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y){

                if(music.isPlaying())
                music.stop();
                game.setScreen(new Menu(game));
            }
        });

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(gameWin, 0, 0, Pixtok.V_WIDTH, Pixtok.V_HEIGHT);
        game.batch.end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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

        gameWin.dispose();
        stage.dispose();
        game.dispose();
        music.dispose();
    }
}