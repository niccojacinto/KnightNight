package com.knightnight.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.knightnight.game.KnightNight;
import com.knightnight.game.Screens.PlayScreen;
import com.knightnight.game.Util.Direction;
import java.lang.*;

import java.util.ArrayList;

/**
 * Created by nix_j on 4/3/2016.
 */
public class Player extends Sprite{

    private static final String TAG = "Player: ";

    KnightNight game;
    enum PlayerState { IDLE, WALKING, ATTACKING, DEAD }
    PlayerState playerState;

    public Vector2 gridPosition;
    public Vector2 position;
    private Vector2 targetPos;
    ArrayList<Animation> animations;
    TextureRegion currentFrame;
    float animTime;
    float moveSpeed;
    boolean isFlipped;
    float lcf;

    public Player (KnightNight _game, int gridX, int gridY) {
        super();
        game = _game;
        playerState = PlayerState.IDLE;

        position = new Vector2(64, 64); // ???
        gridPosition = new Vector2(gridX, gridY);
        isFlipped = false;

        animations = new ArrayList<Animation>(PlayerState.values().length);
        //Gdx.app.debug(TAG, PlayerState.values().length + "");

        animTime = 0;
        moveSpeed = 32.0f;
        lcf = 0f;

        setSize(32, 32);
        initAnimations();
    }

    private void initAnimations() {
        TextureRegion[] animFrames;
        Animation tmpAnim;

        animFrames = new TextureRegion[2];
        animFrames[0] = new TextureRegion((new Texture("Hero/hero_idle0000.png")));
        animFrames[1] = new TextureRegion((new Texture("Hero/hero_idle0001.png")));
        tmpAnim = new Animation(0.25f, animFrames);
        animations.add(tmpAnim);
        animFrames = new TextureRegion[4];
        animFrames[0] = new TextureRegion((new Texture("Hero/hero_walk0000.png")));
        animFrames[1] = new TextureRegion((new Texture("Hero/hero_walk0001.png")));
        animFrames[2] = new TextureRegion((new Texture("Hero/hero_walk0002.png")));
        animFrames[3] = new TextureRegion((new Texture("Hero/hero_walk0003.png")));
        tmpAnim = new Animation(0.25f, animFrames);
        animations.add(tmpAnim);

        animFrames = new TextureRegion[6];
        animFrames[0] = new TextureRegion((new Texture("Hero/hero_attack0000.png")));
        animFrames[1] = new TextureRegion((new Texture("Hero/hero_attack0001.png")));
        animFrames[2] = new TextureRegion((new Texture("Hero/hero_attack0002.png")));
        animFrames[3] = new TextureRegion((new Texture("Hero/hero_attack0003.png")));
        animFrames[4] = new TextureRegion((new Texture("Hero/hero_attack0004.png")));
        animFrames[5] = new TextureRegion((new Texture("Hero/hero_attack0005.png")));
        tmpAnim = new Animation(0.1f, animFrames);
        animations.add(tmpAnim);

    }

    public void onTap(int x, int y)     {
        Vector2 p = new Vector2(x-KnightNight.WIDTH/2, KnightNight.HEIGHT/1.3f-y);
        Direction dir;

        // Gdx.app.debug(TAG, "(" + p.x + ", " + p.y + ")");
        if (Math.abs(p.x) > Math.abs(p.y)) dir = ( p.x <= 0 ) ? Direction.LEFT : Direction.RIGHT;
        else dir = ( p.y <= 0 ) ? Direction.DOWN : Direction.UP;

        move(dir);
    }

    private void move(Direction dir) {
        lcf=0;
        playerState = PlayerState.WALKING;
        switch (dir) {
            case LEFT:
                if (PlayScreen.isFree((int)gridPosition.x-1, (int)gridPosition.y) == 1) { gridPosition.x--; }
                else if (PlayScreen.isFree((int)gridPosition.x-1, (int)gridPosition.y) == 0) {playerState = PlayerState.ATTACKING;}
                isFlipped = true;
                break;
            case RIGHT:
                if (PlayScreen.isFree((int)gridPosition.x+1, (int)gridPosition.y) == 1) { gridPosition.x++; }
                else if (PlayScreen.isFree((int)gridPosition.x+1, (int)gridPosition.y) == 0) {playerState = PlayerState.ATTACKING;}
                isFlipped = false;
                break;
            case UP:
                if (PlayScreen.isFree((int)gridPosition.x, (int)gridPosition.y+1)== 1) { gridPosition.y++; }
                else if (PlayScreen.isFree((int)gridPosition.x, (int)gridPosition.y+1) == 0) {playerState = PlayerState.ATTACKING;}
                isFlipped = false;
                break;
            case DOWN:
                if (PlayScreen.isFree((int)gridPosition.x, (int)gridPosition.y-1) == 1) {gridPosition.y--;}
                else if (PlayScreen.isFree((int)gridPosition.x, (int)gridPosition.y-1) == 0) {playerState = PlayerState.ATTACKING;}
                isFlipped = true;
                break;
            default:
                break;
        }
    }

    public void update(float delta) {
        animTime += Gdx.graphics.getDeltaTime();
        if (lcf >= 1 && playerState != PlayerState.ATTACKING) {
            playerState = PlayerState.IDLE;
            lcf = 0;

        }

        lcf += 2 * delta;
        // Gdx.app.debug(TAG, "lcf: " + lcf);
        position.set(position.lerp(new Vector2(gridPosition.x *32, gridPosition.y*32), lcf));
        setPosition(position.x, position.y);

    }


    public void render(float delta) {
        update(delta);

        currentFrame = animations.get(playerState.ordinal()).getKeyFrame(animTime, true);

        if(!currentFrame.isFlipX() && isFlipped) {
            currentFrame.flip(true, false);
        } else if (currentFrame.isFlipX() && !isFlipped) {
            currentFrame.flip(true, false);
        }


        setRegion(currentFrame);
        draw(game.batch);
    }
}
