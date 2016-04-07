package com.knightnight.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.knightnight.game.KnightNight;

/**
 * Created by Totomato on 4/6/2016.
 */
public class Key extends Sprite implements IPosition {

    private static KnightNight game;
    private static Texture tex = new Texture("Objects/key.png");

    public Key(KnightNight _game, int x, int y) {
        super(tex);
        game = _game;
        setPosition(x * 32, y * 32);
        setSize(32, 32);
        setOriginCenter();
    }

    public int X(){
        return (int)(getX() / 32);
    }

    public int Y(){
        return (int)(getY() / 32);
    }

}
