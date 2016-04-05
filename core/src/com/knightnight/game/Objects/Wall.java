package com.knightnight.game.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.knightnight.game.KnightNight;

/**
 * Created by nix_j on 4/5/2016.
 */
public class Wall extends Sprite {

    private static KnightNight game;

    public Wall(KnightNight _game, int x, int y) {
        super(new Texture("wall.png"));
        game = _game;
        setPosition(x * 32, y * 32);
        setSize(32, 32);
    }

}
