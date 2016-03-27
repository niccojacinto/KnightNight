package com.niccojacinto.knightnight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.niccojacinto.knightnight.util.KNAssetManager;

/**
 * Created by nix_j on 3/25/2016.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = "GameView: ";

    public static Rect screenRect;

    public Context context;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private KNAssetManager knAssetManager;

    Game game;

    public GameView(Context _context) {
        super(_context);
        context = _context;

        knAssetManager = new KNAssetManager(_context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(this);

        Log.d(TAG, "constructed");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Rect screenRect = new Rect(holder.getSurfaceFrame());

        //Nicco: Trying to use screenRect as a static variable of GameView instead of passing it to pretty much everything.
        screenRect = new Rect(holder.getSurfaceFrame()); // static
        game = new Game(this, screenRect);

        gameLoopThread.setRunning(true);
        gameLoopThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameLoopThread.setRunning(false);
        while (retry) {
            try {
                gameLoopThread.join();
                retry = false;
            } catch (InterruptedException e) { }
        }
    }

    public void loop(float deltaTime, Canvas canvas) {
        if (canvas != null) game.loop(deltaTime, canvas);
    }
}
