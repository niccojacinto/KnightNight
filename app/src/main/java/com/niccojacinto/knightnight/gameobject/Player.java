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

    public Player(Vector2 gridStartPos) {

        super(R.drawable.player, gridStartPos, 200);
        playerState = PlayerState.IDLE;
        direction = Direction.RIGHT;


        // TODO: maybe add a spritesheet class, though I dont really wanna go through it.

        anims = new ArrayList<Animation>();
        for (int i = 0; i < PlayerState.values().length; ++i) {
            anims.add(new Animation());
        }

        int scale=64;

        anims.get(PlayerState.IDLE.ordinal()).addFrame(R.drawable.hero_idle0000,scale);
        anims.get(PlayerState.IDLE.ordinal()).addFrame(R.drawable.hero_idle0001,scale);
        anims.get(PlayerState.MOVING.ordinal()).addFrame(R.drawable.hero_walk0000,scale);
        anims.get(PlayerState.MOVING.ordinal()).addFrame(R.drawable.hero_walk0001,scale);
        anims.get(PlayerState.MOVING.ordinal()).addFrame(R.drawable.hero_walk0002,scale);
        anims.get(PlayerState.MOVING.ordinal()).addFrame(R.drawable.hero_walk0003,scale);
        anims.get(PlayerState.ATTACKING.ordinal()).addFrame(R.drawable.hero_attack0000,scale);
        anims.get(PlayerState.ATTACKING.ordinal()).addFrame(R.drawable.hero_attack0001,scale);
        anims.get(PlayerState.ATTACKING.ordinal()).addFrame(R.drawable.hero_attack0002,scale);
        anims.get(PlayerState.ATTACKING.ordinal()).addFrame(R.drawable.hero_attack0003,scale);
        anims.get(PlayerState.ATTACKING.ordinal()).addFrame(R.drawable.hero_attack0004,scale);
        anims.get(PlayerState.ATTACKING.ordinal()).addFrame(R.drawable.hero_attack0005,scale);
        anims.get(PlayerState.DEAD.ordinal()).addFrame(R.drawable.player,scale);



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

        playerState = PlayerState.MOVING;
    }

    public void update(float deltaTime) {
        super.update(deltaTime);

        // Using Ordinal here, make sure the animations are loaded in the right order as the enum
        anims.get(playerState.ordinal()).Play(deltaTime);
        texture =  anims.get(playerState.ordinal()).getFrame();
        if (direction == Direction.LEFT)
            texture = KNAssetManager.flip(texture);

    }

    @Override
    public void draw(Canvas canvas) {


        super.draw(canvas);

    }
}
