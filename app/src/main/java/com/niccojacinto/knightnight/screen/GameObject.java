package com.niccojacinto.knightnight.screen;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.niccojacinto.knightnight.GameView;
import com.niccojacinto.knightnight.util.KNAssetManager;
import com.niccojacinto.knightnight.util.Vector2;

/**
 * Created by nix_j on 3/26/2016.
 */
public class GameObject {

    // A GameObject is anything that can be rendered into the screen, this may include:
        // special text (A separate class from String)
        // images

    private final String TAG = "GameObject: ";

    protected Bitmap texture;
    protected Rect imageBounds;
    protected Rect dstRect; // destination rectangle

    protected Vector2 position;

    public Vector2 getPosition() { return position; }


    // ------------ Constructors ------------
    public GameObject(int textureId) {
        texture = KNAssetManager.loadTexture(textureId);
        Rect sRect = GameView.screenRect;
        position = new Vector2(sRect.width()/2, sRect.height()/2); // Center GameObject on Screen
        imageBounds = new Rect(0, 0, texture.getWidth(), texture.getHeight()); // Define a Rect from the texture's pixel dimensions

        // Destination Rectangle: defines a rectangle relative to the object's position and its dimensions
        // Nicco: I was confused about the dstRects purpose, looking at the api though-- it seems
        //        the canvas.drawBitmap() uses dstRect to scale the image and fill dstRect's area
        // Reference: http://developer.android.com/reference/android/graphics/Canvas.html                   <--- Last Updated: March 26, 2016
        dstRect = new Rect((int)position.x, (int)position.y, (int)position.x + imageBounds.width(), (int)position.y + imageBounds.height());

        // additional notes: in his code he has  a variable called gamebox, I didn't look much into it but playing the game once, I think it is meant
        // to account for the screen's offset since the screen is shared by a score window and the actual visuals
    }

    public GameObject(int textureId, Vector2 _position) {
        texture = KNAssetManager.loadTexture(textureId);
        Rect sRect = GameView.screenRect;
        position = _position;
        imageBounds = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        dstRect = new Rect((int)position.x, (int)position.y, (int)position.x + imageBounds.width(), (int)position.y + imageBounds.height());
    }

    public GameObject(int textureId, Vector2 _position, int scale) {
        texture = KNAssetManager.loadScaledTexture(textureId, scale);
        Rect sRect = GameView.screenRect;
        position = _position;
        imageBounds = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        dstRect = new Rect((int)position.x, (int)position.y, (int)position.x + scale, (int)position.y + scale);
    }

    // ------------ Constructors END --------



}
