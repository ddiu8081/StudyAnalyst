package mr.study.analyst;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.sql.Time;

public class TimeRecordActivity extends Activity {

    private Button start_butn;
    //    private ImageButton setting_butn;
//    private ImageButton chart_butn;
    private ObjectAnimator oa;
    private Chronometer chronometer;
    private TextView textView;
    private String title;
    private int currntTimeFromMainActivity;
    private int planTimeFromMainActivity;
    private String timeSendToMainActivity;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        id = intent.getLongExtra("id",0);

        TodoItem thisItem = LitePal.find(TodoItem.class,id);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time_record);
        start_butn=(Button)findViewById(R.id.start_button);

        chronometer = (Chronometer) findViewById(R.id.timer);
        textView=(TextView) findViewById(R.id.tx);

        title = thisItem.getName();
        planTimeFromMainActivity = thisItem.getPlanTime();
        currntTimeFromMainActivity = thisItem.getActuTime();
        textView.setText(title);

    }

    @Override
    protected void onStart() {
        super.onStart();

        onClickStartButn(findViewById(R.id.start_button));
    }

    int onStartButnPress=0;
    int current = 0;
    int newMin = 0;
    boolean startButnPressed = false;
    boolean firstBegin = true;
    boolean comfirm = false;
    public void onClickStartButn(View view) {

        vibrate();

        if (!startButnPressed) {
            startButnPressed=true;
            Toast.makeText(getApplicationContext(), "开始任务", Toast.LENGTH_SHORT).show();
            start_butn.setText("进行中");
            oa =ObjectAnimator.ofFloat(start_butn,"Alpha",1.0f,0.25f,1.0f);
            oa.setDuration(5000);//ms
            oa.setRepeatCount(-1);
            oa.setRepeatMode(ValueAnimator.RESTART);
            oa.setInterpolator(new AccelerateDecelerateInterpolator());//accelerate
            oa.start();
            if(firstBegin){
                firstBegin=false;
            }
//            else if(!comfirm){
//                comfirm = true;
//                Toast.makeText(this,"再次点击学习时间将清零",Toast.LENGTH_LONG).show();
//            }
//            else if(comfirm){
//                comfirm = false;
//                current = -2;
//            }
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    current++;
                    newMin = current / 60 + currntTimeFromMainActivity;
                    chronometer.setText("已专注" + newMin + "分钟");
                }
            });
            chronometer.start();

//             textView.setText(FormatHMS(current));
//             current = 0;
//             chronometer.stop();
//             onStartButnPress = 0;
        }
        else{
            Toast.makeText(getApplicationContext(), "已暂停", Toast.LENGTH_SHORT).show();
            start_butn.setText("暂停中");
            start_butn.clearAnimation();
            oa.end();
            chronometer.stop();
            startButnPressed=false;

        }
        SensorManager sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(new SensorEventListener() {

            public void onSensorChanged(SensorEvent event) {
                if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
                    return;
                }

                float[] values = event.values;
                float ax = values[0];
                float ay = values[1];
                float az = values[2];

                if(az >= 0) {
                    // 取消反面朝上
                    if (startButnPressed) {
                        onClickStartButn(findViewById(R.id.start_button));
                    }
                }
                if(Math.abs(ax) < 0.5 && Math.abs(ay) < 0.5 && az < -9) {
                    // 反面朝上
                    if (!startButnPressed) {
                        onClickStartButn(findViewById(R.id.start_button));
                    }
                }
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();

        TodoItem thisItem = LitePal.find(TodoItem.class,id);
        thisItem.setActuTime(newMin);
        thisItem.save();

        if (current >= 60) {
            ActivityItem activityItem = new ActivityItem();
            activityItem.setName(thisItem.getName());
            activityItem.setDuringTime(current/60);
            activityItem.save();

            Toast.makeText(getApplicationContext(), "已保存记录", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "专注未满一分钟，暂不记录", Toast.LENGTH_SHORT).show();
        }
    }

//    public static String FormatHMS(int time){
//        String hh=time / 3600 > 9 ? time / 3600 +"":"0"+time / 3600;
//        String mm=(time % 3600) /60 > 9 ? (time % 3600) / 60 +"":"0" + (time% 3600) / 60;
//        String ss=(time % 3600) % 60 > 9 ? (time % 3600) % 60 +"":"0" + (time% 3600) % 60;
//        return hh+":"+mm+":"+ss;
//    }

//
//    public void onClickSettingButn(View view){
//            RoatAnimUtil.startAnimationIn(setting_butn);
//            RoatAnimUtil.startAnimationOut(setting_butn);
//            Intent intent1 = new Intent(MainActivity.this,
//                Setting.class);
//            startActivity(intent1);
//    }
//
//    public void onClickChartButn(View view){
//            RoatAnimUtil.startAnimationIn(chart_butn);
//            RoatAnimUtil.startAnimationOut(chart_butn);
//            Intent intent1 = new Intent(MainActivity.this,
//                planTimeCount.class);
//            startActivity(intent1);
//    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else {
            vibrator.vibrate(200);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}