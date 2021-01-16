package edu.sjsu.android.accelerometer;

import android.util.Log;

public class Particle {
    private static final float COR = 0.7f;
    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;
    public void updatePosition(float sx, float sy, float sz, long timestamp) {
        Log.d("timestamp" , timestamp + "");
        float dt = (System.nanoTime() - timestamp) / 1000000000.0f;//10000000.0f;
        Log.d("timestampafter" ,  dt + "");
        mVelX += -sx * dt;
        Log.d("mVelX" ,  mVelX + "");
        mVelY += -sy * dt;
        Log.d("mVelY" ,  mVelY + "");
        mPosX += mVelX * dt;
        Log.d("mX" ,  mPosX + "");
        mPosY += mVelY * dt;
        Log.d("mY" ,  mPosY + "");

    }
    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        final float xmax = mHorizontalBound;
        final float ymax = mVerticalBound;
        final float x = mPosX;
        final float y = mPosY;
        if (mPosX > mHorizontalBound) {
            mPosX = mHorizontalBound;
            mVelX = -mVelX * COR;
        } else if (mPosX < -mHorizontalBound) {
            mPosX = -mHorizontalBound;
            mVelX = -mVelX * COR;
        }
        if (mPosY > mVerticalBound) {
            mPosY = mVerticalBound;
            mVelY = -mVelY * COR;
        } else if (mPosY < -mVerticalBound) {
            mPosY = -mVerticalBound;
            mVelY = -mVelY * COR;
        }
    }
}