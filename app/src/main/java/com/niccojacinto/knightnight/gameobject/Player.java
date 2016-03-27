package com.niccojacinto.knightnight.gameobject;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;

import com.niccojacinto.knightnight.GameView;
import com.niccojacinto.knightnight.R;
import com.niccojacinto.knightnight.util.Animation;
import com.niccojacinto.knightnight.util.KNAssetManager;
import com.niccojacinto.knightnight.util.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nix_j on 3/26/2016.
 */
public class Player extends Sprite {

    enum PlayerState { IDLE, MOVING, ATTACKING, DEAD };
    enum Direction { LEFT, RIGHT };
    private List<Animation> anims;
    private float moveSpeed; // the time it takes to get from one tile to an adjacent tile
    private PlayerState playerState;
    private Direction direction;
    Matrix flipHorizontalMatrix; // Used to flip the direction of texture

    public Player(Vector2 gridStartPos) {

        super(R.drawable.player, gridStartPos);
        playerState = PlayerState.IDLE;
        direction = Direction.RIGHT;


        // TODO: maybe add a spritesheet class, though I dont really wanna go through it.

        anims = new ArrayList<Animation>();
        for (int i = 0; i < PlayerState.values().length; ++i) {
            anims.add(new Animation());
        }

        anims.get(PlayerState.IDLE.ordinal()).addFrame(R.drawable.player);
        anims.get(PlayerState.MOVING.ordinal()).addFrame(R.drawable.player);
        anims.get(PlayerState.ATTACKING.ordinal()).addFrame(R.drawable.player);
        anims.get(PlayerState.DEAD.ordinal()).addFrame(R.drawable.player);



        // anim.addFrame(R.drawable.wall);
    }

    public void receiveTouchEvent(Point touchPos) {
        if (touchPos.x < GameView.screenRect.width()/2) {
            direction = Direction.LEFT;
            position.x -= 32;
        } else {

            direction = Direction.RIGHT;
            position.x += 32;
        }
    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        // Using Ordinal here, make sure the animations are loaded in the right order as the enum
        anims.get(playerState.ordinal()).Play(deltaTime);
        texture =  anims.get(PlayerState.IDLE.ordinal()).getFrame();

    }

    @Override
    public void draw(Canvas canvas) {
        if (direction == Direction.LEFT)
            texture = KNAssetManager.flip(texture);

        super.draw(canvas);

    }
}
