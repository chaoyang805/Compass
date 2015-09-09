package com.chaoyang805.compass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaoyang805.compass.utils.CompassHelper;
import com.chaoyang805.compass.view.CompassView;

public class MainActivity extends AppCompatActivity implements CompassHelper.OnDirectionChangedListener {

    /**
     * 指示方向的imageview
     */
    private ImageView mIvCompass;
    /**
     * 具有刻度盘的指南针view
     */
    private CompassView mCompassView;
    /**
     * 显示方向信息的textview
     */
    private TextView mTvDirection;
    /**
     * 指南针的帮助类对象
     */
    private CompassHelper mHelper;
    private int mLastAngle;
    private int mCurrentAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initViews();
        //实例化帮助类对象
        mHelper = new CompassHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册回调
        mHelper.addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //取消注册传感器回调
        mHelper.removeListener();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        mIvCompass = (ImageView) findViewById(R.id.iv_compass);
        mTvDirection = (TextView) findViewById(R.id.tv_direction);
        mCompassView = (CompassView) findViewById(R.id.compass_view);
    }

    /**
     * 根据角度值判断当前手机的方向,控制控件的显示
     *
     * @param angle
     */
    private void resolveAngle(int angle) {
        //0°表示正北，90°表示正东，180°/-180°表示正南，-90°表示正西
        //手机的方向在正北
//        if (angle == 0) {
//            mTvDirection.setText(String.format(getString(R.string.north)));
//            //北偏东 angle°
//        }else if (0 < angle && angle <= 45) {
//            mTvDirection.setText(String.format(getString(R.string.north_to_east),angle));
//            //东偏北
//        } else if (45 < angle && angle < 90) {
//            mTvDirection.setText(String.format(getString(R.string.east_to_north),90 - angle));
//            //正东
//        } else if (angle == 90) {
//            mTvDirection.setText(String.format(getString(R.string.east)));
//            //东偏南
//        } else if (90 < angle && angle <= 135) {
//            //
//            mTvDirection.setText(String.format(getString(R.string.east_to_south),angle - 90));
//            //南偏东
//        } else if (135 < angle && angle < 180) {
//            mTvDirection.setText(String.format(getString(R.string.south__to_east),180 - angle));
//            //正南
//        } else if (angle == 180 || angle == -180) {
//            mTvDirection.setText(String.format(getString(R.string.south)));
//            //南偏西
//        } else if (-180 < angle && angle < -135) {
//            mTvDirection.setText(String.format(getString(R.string.south_to_west),180 + angle));
//            //西偏南
//        } else if (-135 <= angle && angle < -90) {
//            mTvDirection.setText(String.format(getString(R.string.west_to_south),-90 - angle));
//            //正西
//        } else if (angle == -90) {
//            mTvDirection.setText(String.format(getString(R.string.west)));
//            //西偏北
//        } else if (-90 < angle && angle < -45) {
//            mTvDirection.setText(String.format(getString(R.string.west_to_north),angle + 90));
//            //北偏西
//        } else if (-45 <= angle && angle < 0) {
//            mTvDirection.setText(String.format(getString(R.string.north_to_west),- angle));
//        }
        //让箭头反向旋转一个角度，使其一直指向北方
//        mIvCompass.setRotation(-angle);

        mCompassView.setRotate(-angle);
    }

    @Override
    public void onDirectionChanged(int angle) {
        mCurrentAngle = angle;
        //到两次的角度相同时，不再进行刷新
        if (mLastAngle != mCurrentAngle) {
            Log.d("TAG", "REFRESH");
            resolveAngle(angle);
        }
        mLastAngle = mCurrentAngle;
    }
}
