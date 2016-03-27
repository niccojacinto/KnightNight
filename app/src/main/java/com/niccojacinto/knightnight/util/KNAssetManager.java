package com.niccojacinto.knightnight.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by nix_j on 3/26/2016.
 */
public class KNAssetManager {

    private static Context context;

    public KNAssetManager (Context _context) {
        context = _context;
    }


    public static String loadText(int id) {
        return context.getResources().getString(id);
    }

    public static String[] loadTexts(int id) {
        return context.getResources().getStringArray(id);
    }

    public static Typeface loadFont(String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static Bitmap loadTexture(int id) {
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

    public static Bitmap flip(Bitmap src) {
        Matrix matrix = new Matrix();

        matrix.preScale(-1.0f, 1.0f);

        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


    public static MediaPlayer loadMusic(String assetName) {
        MediaPlayer tmp = new MediaPlayer();
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd(assetName);
            tmp.setDataSource(descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(), descriptor.getLength());
            tmp.prepare();
            tmp.setLooping(true);
        } catch (IOException e) {
            tmp = null;
        }

        return tmp;
    }



    // public MediaPlayer loadSFX() {}
}
