package com.knightnight.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
    PlayScreen screen;
    enum PlayerState { IDLE, WALKING, ATTACKING, DEAD }
    PlayerState playerState;

    public Vector2 gridPosition;
    private Vector2 position;

    ArrayList<Animation> animations;
    TextureRegion currentFrame;
    float animTime;
    float moveSpeed;
    boolean isFlipped;
    float lcf;
    Sound slice;

    private int tapCounter;
    private int pauseTimer;
    private boolean hasKey;

    public int health;

    public Player (KnightNight _game, PlayScreen _screen, int gridX, int gridY) {
        super();
        game = _game;
        screen = _screen;
        playerState = PlayerState.IDLE;

        setCenterX(8);
        gridPosition = new Vector2(gridX, gridY);
        setPosition(gridX*32, gridY*32);
        isFlipped = false;

        animations = new ArrayList<Animation>(PlayerState.values().length);
        //Gdx.app.debug(TAG, PlayerState.values().length + "");
        slice = Gdx.audio.newSound(Gdx.files.internal("slice.mp3"));

        animTime = 0;
        moveSpeed = 32.0f;
        lcf = 0f;

        tapCounter = 0;
        hasKey = false;

        setSize(32, 32);
        initAnimations();
        setOriginCenter();

        health = 1;

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

        animFrames = new TextureRegion[1];
        animFrames[0] = new TextureRegion(new Texture("Hero/tombstone.png"));
        tmpAnim = new Animation(5.0f, animFrames);
        animations.add(tmpAnim);

    }

    public void onTap(int x, int y) {
        if (playerState == PlayerState.DEAD || screen.paused) { return; }

        Vector2 p = new Vector2(x-KnightNight.WIDTH/2, KnightNight.HEIGHT/1.3f-y);
        Direction dir;

        // Gdx.app.debug(TAG, "(" + p.x + ", " + p.y + ")");
        if (Math.abs(p.x) > Math.abs(p.y)) dir = ( p.x <= 0 ) ? Direction.LEFT : Direction.RIGHT;
        else dir = ( p.y <= 0 ) ? Direction.DOWN : Direction.UP;

        move(dir);

        if (tapCounter++ >= PlayScreen.NUMBER_OF_TAPS_TO_SPAWN_SLIMES){
            screen.spawnSlimesNearPlayer();
            tapCounter = 0;
        }
    }

    private void move(Direction dir) {
        lcf=0;
        playerState = PlayerState.WALKING;
        switch (dir) {
            case LEFT:
                move(-1,0);
                isFlipped = true;
                break;
            case RIGHT:
                move(1,0);
                isFlipped = false;
                break;
            case UP:
                move(0,1);
                isFlipped = false;
                break;
            case DOWN:
                move(0,-1);
                isFlipped = true;
                break;
            default:
                break;
        }
    }

    private void move(int x, int y) {

        int gridX = (int)gridPosition.x + x;
        int gridY = (int)gridPosition.y + y;

        int isFreeVal = screen.isFree(gridX, gridY);
        Gdx.app.debug(TAG, "Moving to:  " + isFreeVal);
        boolean headingToKey = isFreeVal == PlayScreen.ISFREE_KEY;
        boolean levelComplete = isFreeVal == PlayScreen.ISFREE_ENDPOINT && hasKey;

        if (isFreeVal == PlayScreen.ISFREE_FLOOR || headingToKey || levelComplete) {
            gridPosition.x+=x;
            gridPosition.y+=y;
            if (headingToKey) {
                screen.destroyKey();
                hasKey = true;
            } else if (levelComplete){
                screen.paused = true;
                pauseTimer = 100; //I literally have no idea what unit this is in. It makes zero sense.
            }
        } else if (isFreeVal >= 0) {
            attack(PlayScreen.getEnemy(isFreeVal));
        }
    }

    private void attack(Slime enemy) {

        if (playerState == PlayerState.ATTACKING || enemy.isDead) return;
        slice.play();
        animTime = 0;
        // Testing hits, remove later
        // enemy.setColor(Color.RED);
        playerState = PlayerState.ATTACKING;
        enemy.kill();

        // currAnim.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void update(float delta) {
        pauseTimer -= delta;
        if (screen.paused && pauseTimer <= 0){
            screen.endLevel();
        }

        if (playerState == PlayerState.DEAD) { return; }

        Animation currAnim =  animations.get(playerState.ordinal());

        animTime += Gdx.graphics.getDeltaTime();
        if (playerState != playerState.IDLE) {
            if (currAnim.isAnimationFinished(animTime)) {
                animTime = 0;
                playerState = PlayerState.IDLE;
            }
        }

        if (lcf >= 1 && playerState != PlayerState.ATTACKING) {
            playerState = PlayerState.IDLE;
            lcf = 0;
        }

        lcf += 2 * delta;
        // Gdx.app.debug(TAG, "lcf: " + lcf);
        Vector2 tmp = new Vector2(getX(), getY());
        tmp.set(tmp.lerp(new Vector2(gridPosition.x *32, gridPosition.y*32), lcf));
        setPosition(tmp.x, tmp.y);
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

    public void damage(int amount) {
        health -= amount;
        if (health <= 0) {
            playerState = PlayerState.DEAD;
        }
    }

}
