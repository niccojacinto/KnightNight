package com.niccojacinto.knightnight;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.niccojacinto.knightnight.input.TouchEventDispatcher;
import com.niccojacinto.knightnight.input.TouchHandler;
import com.niccojacinto.knightnight.screen.Screen;
import com.niccojacinto.knightnight.screen.TitleScreen;

/**
 * Created by nix_j on 3/25/2016.
 */
public class Game {

    private final String TAG = "Game: ";

    private enum GameState { SCREEN_TITLE, SCREEN_GAME, SCREEN_GAMEOVER }
    GameState gameState;

    Rect screenRect;
    Point touchPos;

    private Screen currentScreen;

    private TouchHandler touchHandler;
    private TouchEventDispatcher dispatcher;

    public static int tileSize = 32;

    public Screen getScreen() { return currentScreen; }
    public void setScreen(Screen screen) {
        currentScreen = screen;

        if(screen instanceof TitleScreen)
            gameState = GameState.SCREEN_TITLE;
        /*
        else if(screen instanceof InGameScreen)
            gameState = GameState.inGame;
        else if(screen instanceof GameOverScreen)
            gameState = GameState.over;
        */
    }

    public Game (GameView gameView, Rect _screenRect) {
        screenRect = _screenRect;

        initialize(gameView);
        Log.d(TAG, "constructed");
    }

    private void initialize(GameView gameView) {
        dispatcher = new TouchEventDispatcher(this);
        touchHandler = new TouchHandler(gameView, dispatcher);


        setScreen(new TitleScreen(this));
        touchPos = new Point();

    }

    public void loop(float deltaTime, Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        currentScreen.draw(canvas);

        currentScreen.update(deltaTime);
    }

}
