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



import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nix_j on 4/3/2016.
 */
public class Slime extends Sprite{

    private static final String TAG = "Slime: ";
    private static Random rnd =  new Random();
    private static Sound hit;

    KnightNight game;
    enum SlimeState { IDLE, WALKING, ATTACKING, DEAD }
    SlimeState slimeState;

    public Vector2 gridPosition;
    ArrayList<Animation> animations;
    TextureRegion currentFrame;
    float stateChangeTimer;
    float animTime;
    float moveSpeed;
    boolean isFlipped;
    float lcf;

    public boolean isDead;

    public Slime(KnightNight _game, int x, int y) {
        super();
        game = _game;
        slimeState = SlimeState.IDLE;

        setPosition(x * 32, y * 32);
        gridPosition = new Vector2(getX()/32, getY()/32);
        isFlipped = true;

        animations = new ArrayList<Animation>(SlimeState.values().length);
        hit = Gdx.audio.newSound(Gdx.files.internal("slice.mp3"));
        // rnd = new Random();

        animTime = 0;
        moveSpeed = 16.0f;
        lcf = 0f;
        isDead = false;

        setOriginCenter();
        setSize(32, 32);
        initAnimations();
    }

    private void initAnimations() {
        TextureRegion[] animFrames;
        Animation tmpAnim;

        animFrames = new TextureRegion[4];
        // Make the images static.. so its less on memory..
        animFrames[0] = new TextureRegion((new Texture("Slime/slime_idle0000.png")));
        animFrames[1] = new TextureRegion((new Texture("Slime/slime_idle0001.png")));
        animFrames[2] = new TextureRegion((new Texture("Slime/slime_idle0002.png")));
        animFrames[3] = new TextureRegion((new Texture("Slime/slime_idle0003.png")));
        tmpAnim = new Animation(0.25f, animFrames);
        animations.add(tmpAnim);
        animFrames = new TextureRegion[4];
        animFrames[0] = new TextureRegion((new Texture("Slime/slime_idle0000.png")));
        animFrames[1] = new TextureRegion((new Texture("Slime/slime_idle0001.png")));
        animFrames[2] = new TextureRegion((new Texture("Slime/slime_idle0002.png")));
        animFrames[3] = new TextureRegion((new Texture("Slime/slime_idle0003.png")));
        tmpAnim = new Animation(0.25f, animFrames);
        animations.add(tmpAnim);

    }

    public void moveRandom() {

        int num = rnd.nextInt((3 - 0) + 1) + 0;

        if (num==0) move(Direction.UP);
        else if (num==1) move (Direction.DOWN);
        else if (num==2) move(Direction.LEFT);
        else move(Direction.RIGHT);
    }


    private void move(Direction dir) {
        lcf=0;
        slimeState = SlimeState.WALKING;
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

        int isFreeVal = ((PlayScreen)(game.getScreen())).isFree(gridX, gridY);

        if (isFreeVal == PlayScreen.ISFREE_FLOOR) {
            gridPosition.x += x;
            gridPosition.y += y;
        }
    }

    public void kill() {
        if (isDead) return;
        PlayScreen.score++;
        hit.play();
        isDead = true;
        PlayScreen.markForDeletion(this);
    }

    public void update(float delta) {
        stateChangeTimer -= delta;
        if (stateChangeTimer <= 0) {
            int num = rnd.nextInt((5 - 2) + 1) + 2;
            moveRandom();
            stateChangeTimer = num;
        }
        animTime += Gdx.graphics.getDeltaTime();
        if (lcf >= 1) {
            slimeState = SlimeState.IDLE;
            lcf = 0;

        }

        lcf += 2 * delta;
        // Gdx.app.debug(TAG, "lcf: " + lcf);
        Vector2 tmp = new Vector2(getX(), getY());
        tmp.set(tmp.lerp(new Vector2(gridPosition.x *32, gridPosition.y*32), lcf));
        setPosition(tmp.x, tmp.y);
    }


    public void render(float delta) {
        if (isDead) return;
        update(delta);

        currentFrame = animations.get(slimeState.ordinal()).getKeyFrame(animTime, true);

        if(!currentFrame.isFlipX() && isFlipped) {
            currentFrame.flip(true, false);
        } else if (currentFrame.isFlipX() && !isFlipped) {
            currentFrame.flip(true, false);
        }


        setRegion(currentFrame);
        draw(game.batch);
    }
}
