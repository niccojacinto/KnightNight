package com.niccojacinto.knightnight.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;

import com.niccojacinto.knightnight.Game;
import com.niccojacinto.knightnight.GameView;
import com.niccojacinto.knightnight.R;
import com.niccojacinto.knightnight.util.KNAssetManager;

/**
 * Created by nix_j on 3/26/2016.
 */
public class TitleScreen extends Screen {

    private MediaPlayer bgm;
    private Typeface titleFont;

    private String[] title;
    protected Paint[] stringPaints;
    protected Point[] stringPositions;

    public TitleScreen (Game _game) {
        super(R.drawable.hauntedcastle);
        game = _game;
        init();
    }

    @Override
    public void init() {
        bgm = KNAssetManager.loadMusic("medieval.mp3");
        bgm.start();

        title = KNAssetManager.loadTexts(R.array.arr_title);
        stringPaints = new Paint[3];
        stringPositions = new Point[3];
        titleFont = KNAssetManager.loadFont("blackwoodcastle.ttf");

        for(int i=0; i<stringPaints.length; i++)
        {
            if(i < 2) {
                stringPaints[i] = new Paint();
                stringPaints[i].setTypeface(titleFont);
                stringPaints[i].setColor(Color.MAGENTA);
                stringPaints[i].setTextSize(200.0f);
                stringPaints[i].setUnderlineText(false);
                stringPaints[i].setTextAlign(Paint.Align.CENTER);
            }
            else {
                stringPaints[i] = new Paint();
                stringPaints[i].setColor(Color.MAGENTA);
                stringPaints[i].setTextSize(40.0f);
                stringPaints[i].setUnderlineText(true);
                stringPaints[i].setTextAlign(Paint.Align.CENTER);
            }

            stringPositions[i] = new Point(GameView.screenRect.width()/2, (int)(position.y + (GameView.screenRect.height()/3) + (200*i)));
        }
    }

    @Override
    public void receiveTouchEvent(Point touchPos) {
        game.setScreen(new LevelScreen(game));

        //TODO: Deallocate used resources on touch, depending on how garbage collection is handled.
    }

    @Override
    public void draw(Canvas canvas) {
        // Draw BG
        canvas.drawBitmap(texture, imageBounds, dstRect, null);

        // Draw Screen Strings
        for(int i=0; i<title.length; i++)
            canvas.drawText(title[i], (float)stringPositions[i].x, (float)stringPositions[i].y, stringPaints[i]);
    }
}
