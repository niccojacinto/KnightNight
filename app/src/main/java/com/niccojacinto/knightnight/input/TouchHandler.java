package com.niccojacinto.knightnight.input;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Created by nix_j on 3/26/2016.
 */
public class TouchHandler implements OnTouchListener {

    private final String TAG = "TouchHandler: ";

    private boolean isTouched;
    private Point touchPos;

    private TouchEventDispatcher dispatcher;

    public TouchHandler(View view, TouchEventDispatcher _dispatcher) {
        view.setOnTouchListener(this);
        touchPos = new Point();
        dispatcher = _dispatcher;
        Log.d(TAG, "constructed");

    }

    public boolean onTouch(View view, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchPos.set((int)event.getX(), (int)event.getY());
                isTouched = true;
                dispatcher.dispatch(touchPos);
                break;
            case MotionEvent.ACTION_UP:
                isTouched = false;
                break;
        }

        return isTouched;
    }

    public Point getLastTouchPos() { return touchPos; }
    public boolean getIsTouched() {return isTouched; }
}
