package com.knightnight.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.knightnight.game.KnightNight;
import com.knightnight.game.MapGen.MapConstants;
import com.knightnight.game.Objects.Floor;
import com.knightnight.game.Objects.Player;
import com.knightnight.game.Objects.Slime;
import com.knightnight.game.Objects.Wall;
import com.knightnight.game.Objects.Void;
import com.knightnight.game.MapGen.Map;

import java.util.ArrayList;


/**
 * Created by nix_j on 3/31/2016.
 */
public class PlayScreen implements Screen {

    private ArrayList<Sprite> gameObjects;
    private static ArrayList<Slime> enemies;
    private OrthographicCamera cam;
    private KnightNight game;
    // private Texture bg;
    private Player player;
    private Map map;
    private static char[][] mapGrid;
    public PlayScreen(KnightNight _game) {

        game = _game;
        // bg = new Texture("tavern.png");
        cam = new OrthographicCamera(KnightNight.WIDTH, KnightNight.HEIGHT);
        cam.zoom = 0.5f;
        gameObjects = new ArrayList<Sprite>();
        enemies = new ArrayList<Slime>();
        map = new Map(50, 50);
        map.out();
        loadMap(map.getData());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        // game.batch.draw(bg, 0, 0);

        for (int w = 0; w < gameObjects.size(); ++w) {
            gameObjects.get(w).draw(game.batch);
        }

        for (int e = 0; e < enemies.size(); ++e) {
            enemies.get(e).render(delta);
        }

        if (Gdx.input.justTouched()) {
            player.onTap(Gdx.input.getX(), Gdx.input.getY());
        }

        game.batch.setProjectionMatrix(cam.combined);
        cam.position.set(player.position.x, player.position.y, 0);
        cam.update();

        player.render(delta);
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

    }

    private void loadMap(char[][] map) {
        mapGrid = map;
        int mapWidth = map[0].length;
        int mapHeight = map.length;

        for (int x = 0; x < mapWidth; ++x) {
            for (int y = 0; y  < mapHeight; ++y) {
                char s = map[x][y];
                switch (s) {
                    case MapConstants.WALL:
                        Wall tmpw = new Wall(game, x, y);
                        gameObjects.add(tmpw);
                        //Gdx.app.debug("Wall: ", "Created Wall");
                        break;
                    case MapConstants.STARTPOINT:
                        player = new Player(game, x, y);
                    case MapConstants.ENDPOINT:
                    case MapConstants.KEY:
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
        spawnSlimes(10);
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

                if (isFree(x, y) == 1) {
                    Gdx.app.debug("spawnSlime", "Created ("+x + "," + y + ")" );
                    enemies.add(new Slime(game, x, y));
                    break;
                }
            }
        }
    }

    //Returns 1 if the tile is a floor.
    //Returns 0 if there's an enemy on this tile.
    //Returns -1 if it's out of bounds OR it's a void OR static object like a wall.
    public static int isFree(int x, int y) {
        int free = -1;
        if (x < 0 || x > mapGrid[0].length-1 || y < 0 || y > mapGrid.length-1) return free;
        if (mapGrid[x][y] == MapConstants.FLOOR) {
            free = 1;
        }

        for (int e = 0; e < enemies.size(); ++e) {
            // Gdx.app.debug("Wall: ", "("+enemies.get(e).gridPosition.x + "," + enemies.get(e).gridPosition.y + ")" );
            if (enemies.get(e).gridPosition.equals(new Vector2(x, y))) {
                free = 0;

            }
        }

       // Gdx.app.debug("Wall: ", "("+x + "," + y + ")" );
        return free;
    }
}
