package edu.sjsu.android.accelerometer;

import android.util.Log;

public class Particle {
    private static final float COR = 0.7f;
    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;
    public void updatePosition(float sx, float sy, float sz, long timestamp) {
        float dt = (System.nanoTime() - timestamp) / 1000000000.0f; 
        mVelX += -sx * dt;
        mVelY += -sy * dt;
        mPosX += mVelX * dt;
        mPosY += mVelY * dt;
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
