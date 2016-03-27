package com.niccojacinto.knightnight.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nix_j on 3/26/2016.
 */
public class Animation {

    private float durationPerFrame;
    private float transitionTime;
    private List<Bitmap> anim;
    private int index;

    public Animation() {
        index = 0;
        durationPerFrame = 0.5f;
        transitionTime = durationPerFrame;
        anim = new ArrayList<Bitmap>();
    }

    public void addFrame(int texId, int scale) {
        anim.add(KNAssetManager.loadScaledTexture(texId, scale));
    }

    public Bitmap getFrame() {
        return anim.get(index);
    }

    public void Play(float deltaTime) {
        transitionTime -= deltaTime;
        if (transitionTime <= 0) {
            transitionTime = durationPerFrame;
            index = index == anim.size()-1 ? 0 : index+1;
        }

        //Log.d("Anim: ", transitionTime + "");
        //Log.d("Anim: ", index+"");
    }

}
