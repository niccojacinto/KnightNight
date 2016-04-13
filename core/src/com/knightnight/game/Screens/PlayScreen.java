package com.knightnight.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.knightnight.game.KnightNight;
import com.knightnight.game.MapGen.MapConstants;
import com.knightnight.game.Objects.Floor;
import com.knightnight.game.Objects.Player;
import com.knightnight.game.Objects.Slime;
import com.knightnight.game.Objects.Wall;
import com.knightnight.game.Objects.Void;
import com.knightnight.game.Objects.Key;
import com.knightnight.game.Objects.Chest;
import com.knightnight.game.MapGen.Map;

import java.util.ArrayList;


/**
 * Created by nix_j on 3/31/2016.
 */
public class PlayScreen implements Screen {
    public static final int NUMBER_OF_TAPS_TO_SPAWN_SLIMES = 30;
    public boolean paused;

    private ArrayList<Sprite> gameObjects;
    private static ArrayList<Slime> enemies;

    private OrthographicCamera cam;
    private KnightNight game;
    // private Texture bg;
    public static Player player;
    private Key key;
    private Chest chest;
    private Map map;
    private static char[][] mapGrid;

    private Stage stage;

    private static ArrayList<Slime> disposable;
    private float disposeTime = 10.0f;

    //public static int score;
    //BitmapFont scoreFont;
    //FreeTypeFontGenerator generator;
    //FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    //GlyphLayout gl;
    //Label lblScore;
    //Label.LabelStyle lblStyle;

    public PlayScreen(KnightNight _game) {

        game = _game;
        // bg = new Texture("tavern.png");
        cam = new OrthographicCamera(KnightNight.WIDTH, KnightNight.HEIGHT);
        cam.zoom = 0.5f;
        gameObjects = new ArrayList<Sprite>();
        enemies = new ArrayList<Slime>();
        disposable = new ArrayList<Slime>();
        map = new Map(50, 50);
        map.out();
        loadMap(map.getData());
        paused = false;


        //score = 0;
        //generator = new FreeTypeFontGenerator(Gdx.files.internal("font_blackwoodcastle.ttf"));
        //parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //parameter.size = 75;
        //scoreFont = generator.generateFont(parameter); // font size 12 pixels
        //gl = new GlyphLayout(scoreFont, KnightNight.TITLE);

        // FOR UI ELEMENTS
        /*
        stage = new Stage();
        stage.getViewport().setCamera(cam);
        stage.setViewport(new ExtendViewport(KnightNight.WIDTH, KnightNight.HEIGHT));
        lblStyle = new Label.LabelStyle();
        lblStyle.font = scoreFont;
        lblScore = new Label(score+"",lblStyle);
        lblScore.setBounds(0, 0, 20, 20);
        lblScore.setFontScale(10.0f, 10.0f);

        stage.addActor(lblScore);
        */
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // lblScore.setText(score+"");
        if (disposeTime <= 0) {
            updateCurrentEnemies();
            disposeTime = 10.0f;
        }


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();


        // game.batch.draw(bg, 0, 0);

        for (int w = gameObjects.size() - 1; w >= 0; --w) {
            gameObjects.get(w).draw(game.batch);
        }

        for (int e = enemies.size() - 1; e >= 0; --e) {
            enemies.get(e).render(delta);
        }

        if (Gdx.input.justTouched()) {
            player.onTap(Gdx.input.getX(), Gdx.input.getY());
        }


        player.render(delta);


        cam.position.set(player.getX(), player.getY(), 0);
        game.batch.setProjectionMatrix(cam.combined);
        cam.update();
        // stage.draw();
        game.batch.end();
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
        //generator.dispose();

    }

    public void spawnSlimesNearPlayer() {
        spawnSlimes(2);
    }

    private void loadMap(char[][] map) {
        mapGrid = map;
        int mapWidth = map[0].length;
        int mapHeight = map.length;

        for (int x = 0; x < mapWidth; ++x) {
            for (int y = 0; y  < mapHeight; ++y) {
                char s = map[x][y];
                switch (s) {
                    case MapConstants.HALL_WALL:
                    case MapConstants.ROOM_WALL:
                        Wall tmpw = new Wall(game, x, y);
                        gameObjects.add(tmpw);
                        //Gdx.app.debug("Wall: ", "Created Wall");
                        break;
                    case MapConstants.STARTPOINT:
                        player = new Player(game, this, x, y);
                        makeAFloor(x,y);
                        break;
                    case MapConstants.ENDPOINT:
                        chest = new Chest(game, x, y);
                        gameObjects.add(chest);
                        makeAFloor(x,y);
                        break;
                    case MapConstants.KEY:
                        key = new Key(game, x, y);
                        gameObjects.add(key);
                        makeAFloor(x,y);
                        break;
                    case MapConstants.FLOOR:
                        makeAFloor(x,y);
                        break;
                    case MapConstants.VOID:
                        Void tmpv = new Void(game, x, y);
                        gameObjects.add(tmpv);
                        break;
                    default:
                        break;
                }
            }
        }

        //I pray that this function works.
        spawnSlimes(2);
    }

    private void makeAFloor(int x, int y) {
        Floor tmpf = new Floor(game, x, y);
        gameObjects.add(tmpf);
    }

    private void spawnSlimes(int number) {
        if (player == null){
            Gdx.app.debug("spawnSlime: ", "Player is null; slimes cannot be spawned.");
            return;
        }

        for (int i = 0; i < number; i++ ){
            boolean versionHelper = false;
            //It'll try 10 times to spawn this Slime.
            //Version helper is used to improve where the Slimes are spawned. It works like magic.
            for (int attempts = 0; attempts < 10; attempts++) {
                int x = map.getXNear((int) player.gridPosition.x, versionHelper);
                int y = map.getYNear((int) player.gridPosition.y, !versionHelper);
                versionHelper = !versionHelper;
                if (isFree(x, y) == ISFREE_FLOOR) {
                    Gdx.app.debug("spawnSlime", "Created ("+x + "," + y + ")" );
                    enemies.add(new Slime(game, this, x, y));
                    break;
                }
            }
        }
    }

    public static final int ISFREE_PLAYER = -5;
    public static final int ISFREE_FLOOR= -2;
    public static final int ISFREE_KEY= -3;
    public static final int ISFREE_ENDPOINT= -4;
    public static final int ISFREE_DEFAULT= -1;
    //Returns -2 if the tile is a floor.
    //Returns index of enemy if there's an enemy on this tile.
    //Returns -1 if it's out of bounds OR it's a void OR static object like a wall.
    public int isFree(int x, int y) {

        int free = ISFREE_DEFAULT;
        // Check Map Size
        if (x < 0 || x > mapGrid[0].length-1 || y < 0 || y > mapGrid.length-1) return -1;

        // Check Tile Type
        if (mapGrid[x][y] == MapConstants.FLOOR || mapGrid[x][y] == MapConstants.STARTPOINT) {
            free = ISFREE_FLOOR;
        }

        if (player.gridPosition.equals(new Vector2(x, y))) {
            return ISFREE_PLAYER;
        }

        // Check Key
        if (key != null && x == key.X() && y == key.Y()) {
            return ISFREE_KEY;
        }

        // Check Endpoint
        if (chest != null && x == chest.X() && y == chest.Y()) {
            return ISFREE_ENDPOINT;
        }


        // Check spawned enemies
        for (int e = 0; e < enemies.size(); ++e) {
            Slime enemy = enemies.get(e);
            // if (enemy == null) { return ISFREE_FLOOR; }
            if (!enemy.isDead && enemy.gridPosition.equals(new Vector2(x, y))) {
                return e;
            }
        }

        return free;
       // Gdx.app.debug("Wall: ", "("+x + "," + y + ")" );
    }

    public void destroyKey(){
        gameObjects.remove(key);
        key = null;
    }

    public void endLevel(){
        //Game is won!!
        Gdx.app.debug("Game", "Player has successfully completed the level!!!" );
        game.setScreen(new PlayScreen(game));
    }
    public static Slime getEnemy(int index) {
        return enemies.get(index);
    }

    public static void markForDeletion(Slime slime) {
        if (disposable.contains(slime) || slime == null) {
           return;
        }
        disposable.add(slime);
    }

    public void updateCurrentEnemies() {
        for (int i =0; i < disposable.size(); ++i) {
            Slime slime = enemies.get(i);
            if (enemies.contains(slime)) {
                enemies.remove(slime);
            }
        }

        disposable.clear();
    }
}
