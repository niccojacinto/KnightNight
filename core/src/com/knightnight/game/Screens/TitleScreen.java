package com.knightnight.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.knightnight.game.KnightNight;

import java.awt.Font;



/**
 * Created by nix_j on 3/31/2016.
 */
public class TitleScreen implements Screen {

    private final String info = "[ Tap Screen to Continue ]";
    private BitmapFont fontTitle;
    private BitmapFont fontInfo;
    private KnightNight game;
    private Texture bg;
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    GlyphLayout gl;

    public TitleScreen(KnightNight  _game) {
        game = _game;
        // gamePort = new StretchViewport(WIDTH, HEIGHT);
        //fontTitle = new BitmapFont("font_blackwoodcastle.ttf");
        bg = new Texture("hauntedcastle.png");


        generator = new FreeTypeFontGenerator(Gdx.files.internal("font_blackwoodcastle.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 75;
        fontTitle = generator.generateFont(parameter); // font size 12 pixels
        parameter.size = 30;
        fontInfo = generator.generateFont(parameter);
        gl = new GlyphLayout(fontTitle, KnightNight.TITLE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen(game));
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(bg, 0, 0, KnightNight.WIDTH, KnightNight.HEIGHT, 0, 0, KnightNight.WIDTH, KnightNight.HEIGHT, false, false);
        fontTitle.draw(game.batch, KnightNight.TITLE, KnightNight.WIDTH/2 - gl.width/1.8f, KnightNight.HEIGHT/1.5f);
        gl.setText(fontInfo, info);
        fontInfo.draw(game.batch, info, KnightNight.WIDTH/2 - gl.width/2, KnightNight.HEIGHT/2);
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
        game.batch.dispose();
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }

}
