package com.knightnight.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.knightnight.game.KnightNight;
import com.knightnight.game.Objects.Player;

/**
 * Created by nix_j on 3/31/2016.
 */
public class PlayScreen implements Screen {

    private OrthographicCamera cam;
    private KnightNight game;
    private Texture bg;
    private Player player;

    public PlayScreen(KnightNight _game) {
        game = _game;
        bg = new Texture("tavern.png");
        player = new Player(game);
        cam = new OrthographicCamera(KnightNight.WIDTH, KnightNight.HEIGHT);
        cam.zoom -= 0.5f;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        cam.position.set(player.position.x, player.position.y, 0);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        if (Gdx.input.justTouched()) {
            player.onTap(Gdx.input.getX(), Gdx.input.getY());
        }

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
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
}
