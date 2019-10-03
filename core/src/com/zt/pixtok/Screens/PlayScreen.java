package com.zt.pixtok.Screens;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zt.pixtok.Pixtok;
import com.zt.pixtok.Scenes.Hud;
import com.zt.pixtok.Sprites.Monster;
import com.zt.pixtok.Sprites.Pix;
import com.zt.pixtok.Tools.Controller;
import com.zt.pixtok.Tools.WorldContactListener;

public class PlayScreen implements Screen {

    private Pixtok game;

    private OrthographicCamera gamecam;

    private Viewport gamePort;

    private Hud hud;


    private TmxMapLoader maploader;

    private TiledMap map;

    private OrthogonalTiledMapRenderer renderer;

    private World world;

    private Box2DDebugRenderer b2dr;

    private long startTime, timer;
    private int time;

    public static boolean ended;
    public static boolean win;

    private Pix player;

    private Monster monster;

    private TextureAtlas atlasRun;

    private Sound gameOver;

    public static int numberJumps;

    private Texture b1;
    private Texture b2;
    private Texture b3;
    private Texture b4;
    private Texture b5;

    private Controller controller;

    public PlayScreen(Pixtok game) {

        ended = false;
        win = false;

        gameOver = Pixtok.manager.get("sounds/game_over.wav", Sound.class);

        atlasRun = new TextureAtlas("Mario_and_Enemies.pack");
        startTime = TimeUtils.nanoTime();
        timer = TimeUtils.millis();
        time = 0;
        numberJumps = 0;

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Pixtok.V_WIDTH/Pixtok.PPM, Pixtok.V_HEIGHT/Pixtok.PPM, gamecam);
        gamePort.apply();
        hud = new Hud(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("mappaJungla.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/Pixtok.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        player = new Pix(world , this);
        monster = new Monster(world);

        //ground
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(((rect.getX() + rect.getWidth() / 2) + 1.5f )/Pixtok.PPM, ((rect.getY() + rect.getHeight() / 2) -0.5f)/Pixtok.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(((rect.getWidth() / 2) - 1.5f)/Pixtok.PPM, (rect.getHeight() / 2)/Pixtok.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
            body.createFixture(fdef).setUserData("Ground");

        }

        //spikes
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) /Pixtok.PPM, (rect.getY() + rect.getHeight() / 2) /Pixtok.PPM);

            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2 )/Pixtok.PPM, (rect.getHeight() / 2) /Pixtok.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            body.createFixture(fdef).setUserData("Spikes");
        }

        //jump
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) /Pixtok.PPM, (rect.getY() + rect.getHeight() / 2) /Pixtok.PPM);

            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2 )/Pixtok.PPM, (rect.getHeight() / 2) /Pixtok.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            body.createFixture(fdef).setUserData("Jump");
            body.createFixture(fdef).setUserData("Jump");
        }

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Pixtok.PPM, (rect.getY() + rect.getHeight() / 2) / Pixtok.PPM);

            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Pixtok.PPM, (rect.getHeight() / 2) / Pixtok.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            body.createFixture(fdef).setUserData("Win");
        }


        //background
        b1 = new Texture("plx-1.png");
        b2 = new Texture("plx-2.png");
        b3 = new Texture("plx-3.png");
        b4 = new Texture("plx-4.png");
        b5 = new Texture("plx-5.png");


        controller = new Controller(game);

        world.setContactListener(new WorldContactListener(player, monster));

    }

    public TextureAtlas getAtlasRun(){
        return atlasRun;
    }

    @Override
    public void show() {

    }

    private void moveMapAndMonsterIncrementTime(float dt) {


       if(TimeUtils.timeSinceNanos(startTime) > 10000000) {
            gamecam.position.x += (Pixtok.VELOCITY * dt /Pixtok.PPM);
            if(monster.b2body.getLinearVelocity().x < 0.9f && monster.isRunningRight())
           monster.b2body.applyLinearImpulse(new Vector2(0.15f, 0), monster.b2body.getWorldCenter(), true);
            else if(monster.b2body.getLinearVelocity().x > -0.9f && !monster.isRunningRight())
                monster.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), monster.b2body.getWorldCenter(), true);

            startTime = TimeUtils.nanoTime();
        }

        if(TimeUtils.timeSinceMillis(timer) >= 1000 * (time+1)){
            hud.setTime(Integer.toString(++time));
        }



    }

    private void handleInput(){

        if((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || controller.getUpPressed()) && numberJumps < 2)
        {
            controller.setUpPressed(false);player.b2body.applyLinearImpulse(new Vector2(0, 2.5f), player.b2body.getWorldCenter(), true); numberJumps++;
        }
        if(( Gdx.input.isKeyPressed(Input.Keys.RIGHT) || controller.isRightPressed()) && player.b2body.getLinearVelocity().x < 1.20f) {
            player.b2body.applyLinearImpulse(new Vector2(0.05f, 0), player.b2body.getWorldCenter(), true);
        }
        if(( Gdx.input.isKeyPressed(Input.Keys.LEFT) || controller.isLeftPressed()) && player.b2body.getLinearVelocity().x >= -1.20f) {
            player.b2body.applyLinearImpulse(new Vector2(-0.05f, 0), player.b2body.getWorldCenter(), true);
        }



    }

    private void update(float dt) {

        if(ended || player.getY() < -2) {
            hud.stopMusic();
            gameOver.play(.35f);
            try {
                Thread.sleep(2300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.setScreen(new GameOverScreen(game));
        }
        if (win){
            hud.stopMusic();
            game.setScreen(new YouWinScreen(game));
        }

        moveMapAndMonsterIncrementTime(dt);
        handleInput();
        world.step(1/60f, 6, 2);

        player.update(dt);
        monster.update(dt, player);


        gamecam.update();
        renderer.setView(gamecam);

    }



    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(b1, 0,0);
        game.batch.draw(b1, 100, 0);
        game.batch.draw(b2,0,0);
        game.batch.draw(b2,300,0);
        game.batch.draw(b3, 0, 0);
        game.batch.draw(b3, 300, 0);
        game.batch.draw(b4, 0 ,0);
        game.batch.draw(b4, 300 ,0);
        game.batch.draw(b5, 0, 0);
        game.batch.draw(b5, 300 ,0);
        game.batch.end();

        renderer.render();

        //render box2d
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        monster.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        if(Gdx.app.getType().equals(Application.ApplicationType.Android))
        controller.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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

        hud.dispose();
        gameOver.dispose();
        game.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        atlasRun.dispose();
        b1.dispose();
        b2.dispose();
        b3.dispose();
        b4.dispose();
        b5.dispose();
        monster.dispose();
        player.dispose();
        if(Gdx.app.getType().equals(Application.ApplicationType.Android))
            controller.dispose();
    }
}