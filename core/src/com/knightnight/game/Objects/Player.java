package com.knightnight.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.knightnight.game.KnightNight;

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
    //ArrayList<Texture> spritesheets;
    TextureRegion currentFrame;
    float animTime;
    float moveSpeed;

    public Player (KnightNight _game) {
        super();
        game = _game;
        playerState = PlayerState.IDLE;
        gridPosition = new Vector2(0, 0);
        position = new Vector2(50, 50);
        targetPos = position;


        animations = new ArrayList<Animation>(PlayerState.values().length);
        //spritesheets = new ArrayList<Texture>(PlayerState.values().length);
        //Gdx.app.debug(TAG, PlayerState.values().length + "");

        animTime = 0;
        moveSpeed = 0.5f;

        setSize(64, 64);
        setPosition(KnightNight.WIDTH / 2, KnightNight.HEIGHT / 2);
        initAnimations();
    }

    private void initAnimations() {
        TextureRegion[] animFrames;
        Animation tmpAnim;

        animFrames = new TextureRegion[2];
        animFrames[0] = new TextureRegion((new Texture("Hero/hero_idle0000.png")));
        animFrames[1] = new TextureRegion((new Texture("Hero/hero_idle0001.png")));
        tmpAnim = new Animation(1.0f, animFrames);
        animations.add(tmpAnim);

    }

    public void onTap(int x, int y) {
        Vector2 endPos = new Vector2(x, y);
        Vector2 normalDir = endPos.sub(position);
        move(normalDir);
    }

    private void move(Vector2 newPos) {
        targetPos = newPos;
    }

    public void update(float delta) {
        animTime += Gdx.graphics.getDeltaTime();
        // position.lerp(targetPos, 1);
        setPosition(position.x, position.y);
    }

    public void render(float delta) {
        update(delta);

        currentFrame = animations.get(playerState.ordinal()).getKeyFrame(animTime, true);  // #16
        setRegion(currentFrame);
        draw(game.batch);
    }
}
