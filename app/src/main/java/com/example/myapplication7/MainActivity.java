package com.example.myapplication7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity   implements SensorEventListener{
    private SensorManager mSensorManager;
    EditText etOrientation,etGyro,etMagnetic,etGravity,etLinearAcc,etTemerature,etLight,etPressure;
    private float[] values, r, gravity, geomagnetic;
    Button compass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取界面上的EditText组件
        etOrientation=findViewById(R.id.etOrientation);
        etGyro=findViewById(R.id.etGyro);
        etMagnetic=findViewById(R.id.etMagnetic);
        etGravity=findViewById(R.id.etGravity);
        etLinearAcc=findViewById(R.id.etLinearAcc);
        etTemerature=findViewById(R.id.etTemerature);
        etLight=findViewById(R.id.etLight);
        etPressure=findViewById(R.id.etPressure);
        compass=findViewById(R.id.compass);
        compass.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent(MainActivity.this, Compass.class);
                                           startActivity(intent);
                                       }
                                   });
        //获取传感器管理服务
        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        values = new float[3];//用来保存最终的结果
        gravity = new float[3];//用来保存加速度传感器的值
        r = new float[9];//
        geomagnetic = new float[3];//用来保存地磁传感器的值
    }
    protected void onResume() {

        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
        //为系统的陀螺仪传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_GAME);
        //为系统的磁场传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_GAME);
        //为系统的重力传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_GAME);
        //为系统的线性加速度传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_GAME);
        //为系统的温度传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),SensorManager.SENSOR_DELAY_GAME);
        //为系统的光传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_GAME);
        //为系统的压力传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),SensorManager.SENSOR_DELAY_GAME);

    }

    protected void onStop() {
        //程序退出时取消注册传感器监听器
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    protected void onPause(){
        //程序暂停时取消注册传感器监听器
        mSensorManager.unregisterListener(this);
        super.onPause();
    }


    public void getValue() {
        // r从这里返回
        SensorManager.getRotationMatrix(r, null, gravity, geomagnetic);
        //values从这里返回
        SensorManager.getOrientation(r, values);
        //提取数据
        double azimuth = Math.toDegrees(values[0]);
        if (azimuth<0) {
            azimuth=azimuth+360;
        }
        double pitch = Math.toDegrees(values[1]);
        double roll = Math.toDegrees(values[2]);
        //方向传感器
        etOrientation.invalidate();
        etOrientation.setText("绕Z轴转过的角度：" + (int)azimuth + "\n绕X轴转过的角度：" + (int)pitch + "\n绕Y轴转过的角度：" + (int)roll);
    }
    //当传感器精度改变时回调该方法
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[]values=event.values;
        //获取触发event的传感器类型
        int sensorType=event.sensor.getType();
        StringBuilder sb=null;
        //判断是哪个传感器发生改变
        switch (sensorType){
            /*方向传感器
            case Sensor.TYPE_ORIENTATION:
                sb=new StringBuilder();
                sb.append("绕Z轴转过的角度：");
                sb.append(values[0]);
                sb.append("\n绕X轴转过的角度：");
                sb.append(values[1]);
                sb.append("\n绕Y轴转过的角度：");
                sb.append(values[2]);
                etOrientation.setText(sb.toString());
                break;*/
                //陀螺仪传感器
            case Sensor.TYPE_GYROSCOPE:
                sb=new StringBuilder();
                sb.append("绕X轴旋转的角速度：");
                sb.append(values[0]);
                sb.append("\n绕Y轴旋转的角速度：");
                sb.append(values[1]);
                sb.append("\n绕Z轴旋转的角速度：");
                sb.append(values[2]);
                etGyro.setText(sb.toString());
                break;
                //磁场传感器
            case Sensor.TYPE_MAGNETIC_FIELD:
                sb=new StringBuilder();
                sb.append("X轴方向上的磁场强度：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的磁场强度：");
                sb.append(values[1]);
                sb.append("\nZ轴方向上的磁场强度：");
                sb.append(values[2]);
                etMagnetic.setText(sb.toString());
                geomagnetic=event.values;
                break;
                //重力传感器
            case Sensor.TYPE_GRAVITY:
                sb=new StringBuilder();
                sb.append("X轴方向上的重力：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的重力：");
                sb.append(values[1]);
                sb.append("\nZ轴方向上的重力：");
                sb.append(values[2]);
                etGravity.setText(sb.toString());
                break;
                //线性加速度传感器
            case Sensor.TYPE_LINEAR_ACCELERATION:
                sb=new StringBuilder();
                sb.append("X轴方向上的线性加速度：");
                sb.append(values[0]);
                sb.append("\nY轴方向上的线性加速度：");
                sb.append(values[1]);
                sb.append("\nZ轴方向上的线性加速度：");
                sb.append(values[2]);
                etLinearAcc.setText(sb.toString());
                gravity = event.values;
                getValue();
                break;
                //温度传感器
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sb=new StringBuilder();
                sb.append("当前温度为：");
                sb.append(values[0]);
                etTemerature.setText(sb.toString());
                break;
                //光传感器
            case Sensor.TYPE_LIGHT:
                sb=new StringBuilder();
                sb.append("当前光的强度为：");
                sb.append(values[0]);
                etLight.setText(sb.toString());
                break;
                //压力传感器
            case Sensor.TYPE_PRESSURE:
                sb=new StringBuilder();
                sb.append("当前压力为：");
                sb.append(values[0]);
                etPressure.setText(sb.toString());
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
