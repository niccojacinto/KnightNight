package com.niccojacinto.knightnight.gameobject;

import android.graphics.Canvas;

import com.niccojacinto.knightnight.Game;
import com.niccojacinto.knightnight.screen.GameObject;
import com.niccojacinto.knightnight.util.Vector2;

/**
 * Created by nix_j on 3/26/2016.
 */
public class Sprite extends GameObject {

    // Sprites are GameObjects but are closer to a gameplay perspective more than framework

    protected Vector2 gridPosition; // The Map used in the game is in tiles, boardPosition is the grid based position of the sprite


    protected Sprite (int texId, Vector2 startGridPos) {
        super(texId, new Vector2(startGridPos.x * Game.tileSize, startGridPos.y * Game.tileSize)); // Calculate grid coordinates and place the player there
        gridPosition = startGridPos;
    }

    protected Sprite (int texId, Vector2 startGridPos, int scale) {
        super(texId, new Vector2(startGridPos.x * Game.tileSize, startGridPos.y * Game.tileSize), scale); // Calculate grid coordinates and place the player there
        gridPosition = startGridPos;
    }


    protected void update(float deltaTime) {

        // updates the graphical position of the sprite to the values of x,y
        dstRect.set((int)position.x, (int)position.y, (int)position.x + imageBounds.width(), (int)position.y + imageBounds.height());
    }

    protected void draw(Canvas canvas) {
        canvas.drawBitmap(texture, imageBounds, dstRect, null);
    }
}
