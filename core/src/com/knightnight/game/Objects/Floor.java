package com.knightnight.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.knightnight.game.KnightNight;

/**
 * Created by nix_j on 4/5/2016.
 */
public class Floor extends Sprite {

    private static KnightNight game;
    private static Texture tex = new Texture("knfloor.png");

    public Floor(KnightNight _game, int x, int y) {
        super(tex);
        game = _game;
        setPosition(x * 32, y * 32);
        setSize(32, 32);
        setOriginCenter();
    }

}
