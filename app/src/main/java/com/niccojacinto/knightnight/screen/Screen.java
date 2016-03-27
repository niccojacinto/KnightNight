package com.niccojacinto.knightnight.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.niccojacinto.knightnight.Game;
import com.niccojacinto.knightnight.util.Vector2;

/**
 * Created by nix_j on 3/26/2016.
 */
public class Screen extends GameObject {

    // Screen is a base class that any screen in the game will inherit from
    // Nicco: debating whether to use this as an interface, but from what I can see, it seems a class would make it easier.

    private final String TAG = "GameScreen: ";

    protected Game game;

    public Screen(int textureId) {
        super(textureId, new Vector2(0,0));
        Log.d(TAG, "constructed");
    }

    public void init() {}

    public void receiveTouchEvent(Point touchPos) {}

    public void update(float deltaTime) {}

    public void draw (Canvas canvas) {}


}
