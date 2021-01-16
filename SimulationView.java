package edu.sjsu.android.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.graphics.BitmapFactory.Options;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.sql.Timestamp;

public class SimulationView extends View implements SensorEventListener {
    private Bitmap mField;
    private Bitmap mBasket;
    private Bitmap mBitMAP;
    private Display mDisplay;
    private SensorManager sensorManager;
    private Sensor mSensor;
    private float mXOrigin;
    private float mYOrigin;
    private float mZOrigin;
    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private float mHorizontalBound;
    private float mVerticalBound;
    private long  mSensorTimeStamp;
    private static final int BALL_SIZE = 64;
    private static final int BASKET_SIZE = 80;

    /*
       Draws items on Screen
    */
    public SimulationView(Context context) {
        super(context);
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitMAP = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);
        Options opts = new Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXOrigin = w * 0.5f;
        mYOrigin = w * 0.5f;
        mHorizontalBound = (w - BALL_SIZE) * 0.5f;
        mVerticalBound = (h - BALL_SIZE) * 0.5f;
    }
    
    /*
     Get basketball movements
    */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (mDisplay.getRotation() == Surface.ROTATION_0) {
                mSensorX = event.values[0];
                mSensorY = event.values[1];
                mSensorZ = event.values[2];
            }
            else if(mDisplay.getRotation() == Surface.ROTATION_90) {
                mSensorX = -event.values[1];
                mSensorY = event.values[0];
                mSensorZ = event.values[2];
            }
            mSensorTimeStamp = event.timestamp;
        }
    }
    
    public void startSimulation() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSimulation() {
        sensorManager.unregisterListener(this);
    }

    private Particle mBall = new Particle();

  @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mField, 0, 0, null);
        canvas.drawBitmap(mBasket, mXOrigin  - BASKET_SIZE / 2, mYOrigin - BASKET_SIZE / 2, null);
        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        canvas.drawBitmap(mBitMAP, (mXOrigin - BALL_SIZE / 2) + mBall.mPosX, (mYOrigin - BALL_SIZE / 2) - mBall.mPosY, null);
        float value = (mXOrigin - BALL_SIZE / 2) + mBall.mPosX;
        float value1 = (mYOrigin - BALL_SIZE / 2) - mBall.mPosY;
        invalidate();
    }
}
