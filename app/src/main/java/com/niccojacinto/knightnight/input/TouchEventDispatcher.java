package com.niccojacinto.knightnight.input;

import android.graphics.Point;

import com.niccojacinto.knightnight.Game;

/**
 * Created by nix_j on 3/26/2016.
 */
public class TouchEventDispatcher {

    private Game game;

    public TouchEventDispatcher(Game _game) { game = _game; }

    public void dispatch(Point touchPos) { game.getScreen().receiveTouchEvent(touchPos); }
}
