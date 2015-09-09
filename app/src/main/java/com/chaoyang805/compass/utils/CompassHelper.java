package com.chaoyang805.compass.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by chaoyang805 on 2015/9/6.
 * 指南针帮助类，通过加速度传感器和磁场传感器计算手机的地理位置方向，并实现回调
 */
public class CompassHelper {

    /**
     * 用于管理传感器的manager对象
     */
    private SensorManager mSensormanager;
    /**
     * 加速度传感器对象
     */
    private Sensor mAccelerometer;
    /**
     * 磁场传感器对象
     */
    private Sensor mMagnetic;
    /**
     * 传感器的事件监听
     */
    private SensorEventListener mSensorEventListener;
    /**
     * 保存加速度传感器传来数据的数组
     */
    private float[] mAccelerometerValues = new float[3];
    /**
     * 保存磁场传感器传来的数据的数组
     */
    private float[] mMagneticValues = new float[3];
    /**
     * 保存通过加速度传感器和磁场传感器数据计算出的方向信息
     */
    private float[] mValues = new float[3];
    /**
     * 旋转矩阵，用来计算mValues
     */
    private float[] mR = new float[9];
    /**
     * 手机方向发生变化时的监听
     */
    private OnDirectionChangedListener mDirectionListener;

    /**
     * 构造方法，实例化sensorManager对象和传感器对象
     * @param context
     */
    public CompassHelper(Context context) {
        //获取sensorManager对象
        mSensormanager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetic = mSensormanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /**
     * 手机方向发生改变时的回调接口
     */
    public interface OnDirectionChangedListener{
        void onDirectionChanged(int angle);
    }

    /**
     * 设置监听的方法
     * @param listener
     */
    public void addListener(OnDirectionChangedListener listener) {
        mDirectionListener = listener;
        //实例化SensorEventListener对象
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    mAccelerometerValues = event.values;
                }
                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    mMagneticValues = event.values;
                }
                //根据加速度传感器和磁场传感器的数据计算出旋转矩阵，填充到mR数组中。
                SensorManager.getRotationMatrix(mR, null, mAccelerometerValues, mMagneticValues);
                //根据mR矩阵计算出手机的方向信息保存到mValues中
                SensorManager.getOrientation(mR, mValues);
                int angle = (int) Math.round(Math.toDegrees(mValues[0]));
                if (mDirectionListener != null) {
                    mDirectionListener.onDirectionChanged(angle);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        mSensormanager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensormanager.registerListener(mSensorEventListener, mMagnetic, SensorManager.SENSOR_DELAY_UI);
    }


    /**
     * 取消注册监听
     */
    public void removeListener(){
        mSensormanager.unregisterListener(mSensorEventListener);
        mDirectionListener = null;
    }

}
