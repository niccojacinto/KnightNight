package com.knightnight.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.knightnight.game.KnightNight;
import com.knightnight.game.Objects.Player;
import com.knightnight.game.Objects.Wall;

import java.util.ArrayList;


/**
 * Created by nix_j on 3/31/2016.
 */
public class PlayScreen implements Screen {

    private ArrayList<Sprite> gameObjects;
    private OrthographicCamera cam;
    private KnightNight game;
    private Texture bg;
    private Player player;
    // private Map map;

    public PlayScreen(KnightNight _game) {
        game = _game;
        bg = new Texture("tavern.png");
        player = new Player(game);
        cam = new OrthographicCamera(KnightNight.WIDTH, KnightNight.HEIGHT);
        cam.zoom -= 0.5f;
        gameObjects = new ArrayList<Sprite>();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        for (int w = 0; w < gameObjects.size(); ++w) {
            gameObjects.get(w).draw(game.batch);
        }

        if (Gdx.input.justTouched()) {
            player.onTap(Gdx.input.getX(), Gdx.input.getY());
        }

        game.batch.setProjectionMatrix(cam.combined);
        cam.position.set(player.position.x, player.position.y, 0);
        cam.update();

        game.batch.draw(bg, 0, 0);
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
        int mapWidth = map[0].length;
        int mapHeight = map.length;

        for (int x = 0; x < mapWidth; ++x) {
            for (int y = 0; y  < mapHeight; ++y) {
                char s = map[x][y];
                switch (s) {
                    case 'W':
                        Wall tmp = new Wall(game, x, y);
                        break;
                    default:
                        break;
                }
            }
        }

    }
}
