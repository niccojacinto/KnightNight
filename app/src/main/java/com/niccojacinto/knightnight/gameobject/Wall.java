package com.niccojacinto.knightnight.gameobject;

import android.graphics.Canvas;

import com.niccojacinto.knightnight.R;
import com.niccojacinto.knightnight.util.Vector2;

/**
 * Created by nix_j on 3/26/2016.
 */
public class Wall extends Sprite {

    public Wall(Vector2 startGridPos) {
        super(R.drawable.wall, startGridPos);
    }

    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

}
