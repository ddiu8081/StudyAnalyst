package mr.study.analyst;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
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
        thisItem.setActuTime(current);
        thisItem.save();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_time_record);
        start_butn=(Button)findViewById(R.id.start_button);
//        setting_butn=(ImageButton)findViewById(R.id.setting_Button);
//        chart_butn=(ImageButton)findViewById(R.id.chart_Button);
        chronometer = (Chronometer) findViewById(R.id.timer);
        textView=(TextView) findViewById(R.id.tx);
//        String message = getIntent().getStringExtra("MESSAGE");
//        title=message.substring(0,message.indexOf("#"));
//        planTimeFromMainActivity = Integer.parseInt(message.substring(message.indexOf("#")+1,message.indexOf("_")));
//        currntTimeFromMainActivity = Integer.parseInt(message.substring(message.indexOf("_")+1));
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
    int current;
    boolean startButnPressed = false;
    boolean firstBegin = true;
    boolean comfirm = false;
    public void onClickStartButn(View view) {

        vibrate();

        if (!startButnPressed) {
            startButnPressed=true;
            start_butn.setText("Stop");
            oa =ObjectAnimator.ofFloat(start_butn,"Alpha",1.0f,0.25f,1.0f);
            oa.setDuration(5000);//ms
            oa.setRepeatCount(-1);
            oa.setRepeatMode(ValueAnimator.RESTART);
            oa.setInterpolator(new AccelerateDecelerateInterpolator());//accelerate
            oa.start();
            if(firstBegin){
                current=currntTimeFromMainActivity*60;
                firstBegin=false;
            }
            else if(!comfirm){
                comfirm = true;
                Toast.makeText(this,"再次点击学习时间将清零",Toast.LENGTH_LONG).show();
            }
            else if(comfirm){
                comfirm = false;
                current = -2;
            }
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    current++;
                    chronometer.setText("已专注" + current / 60 + "分钟");
                }
            });
            chronometer.start();

//             textView.setText(FormatHMS(current));
//             current = 0;
//             chronometer.stop();
//             onStartButnPress = 0;
        }
        else{
            start_butn.setText("Start");
            start_butn.clearAnimation();
            oa.end();
            chronometer.stop();
            startButnPressed=false;
            Intent intent =getIntent();
            //这里使用bundle绷带来传输数据
            Bundle bundle = new Bundle();
            if(current < planTimeFromMainActivity*60) {
                timeSendToMainActivity = FormatHMS(current);
                //传输的内容仍然是键值对的形式
                bundle.putString("second",timeSendToMainActivity);//回发的消息
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
            }
            else{
                timeSendToMainActivity = "planSuccess";
                bundle.putString("second",timeSendToMainActivity);//回发的消息
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        TodoItem thisItem = LitePal.find(TodoItem.class,id);
        thisItem.setActuTime(current/60);
        thisItem.save();
    }

    public static String FormatHMS(int time){
        String hh=time / 3600 > 9 ? time / 3600 +"":"0"+time / 3600;
        String mm=(time % 3600) /60 > 9 ? (time % 3600) / 60 +"":"0" + (time% 3600) / 60;
        String ss=(time % 3600) % 60 > 9 ? (time % 3600) % 60 +"":"0" + (time% 3600) % 60;
        return hh+":"+mm+":"+ss;
    }

    public static String FormatMM(int time){
        String hh=time / 3600 > 9 ? time / 3600 +"":"0"+time / 3600;
        String mm=(time % 3600) /60 > 9 ? (time % 3600) / 60 +"":"0" + (time% 3600) / 60;
        return "";
    }

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
}