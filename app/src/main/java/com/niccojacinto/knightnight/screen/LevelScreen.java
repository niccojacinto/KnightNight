package com.niccojacinto.knightnight.screen;

import android.graphics.Canvas;
import android.graphics.Point;

import com.niccojacinto.knightnight.Game;
import com.niccojacinto.knightnight.R;
import com.niccojacinto.knightnight.gameobject.Player;
import com.niccojacinto.knightnight.gameobject.Wall;
import com.niccojacinto.knightnight.util.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nix_j on 3/26/2016.
 */
public class LevelScreen extends Screen {

    // private Map levelMap;

    private Player player;

    private List<Wall> walls;
    // private List<Enemy> enemies;
    // private List<Animation> anims;
    // private List<Item> items;

    public LevelScreen(Game _game) {
        super(R.drawable.tavern);
        game = _game;
        init();
    }

    @Override
    public void init() {
        walls = new ArrayList<Wall>();

        player = new Player(new Vector2(2,2));
        walls.add(new Wall(new Vector2(3,3)));

    }

    @Override
    public void receiveTouchEvent(Point touchPos) {
        // game.setScreen();
        player.receiveTouchEvent(touchPos);
        //TODO: Deallocate used resources on touch, depending on how garbage collection is handled.
    }

    @Override
    public void update(float deltaTime) {
        player.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        // Draw BG
        canvas.drawBitmap(texture, imageBounds, dstRect, null);

        for (int i=0; i<walls.size(); ++i)
            walls.get(i).draw(canvas);

        player.draw(canvas);
    }

}
