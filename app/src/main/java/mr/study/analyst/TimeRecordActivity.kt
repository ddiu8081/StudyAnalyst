package mr.study.analyst

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import org.jetbrains.anko.act
import kotlinx.android.synthetic.main.activity_time_record.*


class TimeRecordActivity : AppCompatActivity(), Runnable {

    private var recLen: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_record)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarDarkMode(act) //设置状态栏白色字体图标
        view_statusBar.layoutParams.height = QMUIStatusBarHelper.getStatusbarHeight(this)

        handler.postDelayed(this,1000)

        timer.base = SystemClock.elapsedRealtime() //计时器清零
        timer.start()

        vibrate()

        val sm = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sm.registerListener(object : SensorEventListener {

            override fun onSensorChanged(event: SensorEvent) {
                if (Sensor.TYPE_ACCELEROMETER !== event.sensor.type) {
                    return
                }

                val values = event.values
                val ax = values[0]
                val ay = values[1]
                val az = values[2]

                if(az >= 0) {
                    // 取消反面朝上
                    sm.unregisterListener(this)
                    vibrate()
                    finish()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

            }
        }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

    }

    private val handler = Handler()
    override fun run() {
        recLen++
        textView_timer.text = recLen.toString()
        handler.postDelayed(this,1000)
    }

    override fun onResume() {
        super.onResume()

        sendNotify(1)
    }

    override fun onPause() {
        super.onPause()

        sendNotify(2)
    }

    private fun vibrate() {
        val vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else {
            vibrator.vibrate(200)
        }
    }

    private fun sendNotify(type: Int) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this,"timeRecorder")

        // Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(this, TodoInfoActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0)
        val pauseIntent = Intent(this, TimeRecordActivity::class.java)
        val pausePendingIntent = PendingIntent.getActivity(this, 0, pauseIntent, 0)

        val notification = builder
                .setContentTitle("当前专注") //标题
                .setContentText("英语") //描述文字
                .setSmallIcon(R.drawable.ic_stat_name) //通知栏图标
                .setShowWhen(false) //显示时间
//                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)) //大图标
                .setColor(resources.getColor(R.color.colorPrimary, null)) //颜色
                .setAutoCancel(false) //进入应用自动消失
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setNumber(1)
                .addAction(R.drawable.ic_stat_name, "暂停",
                        pausePendingIntent)

        if (type == 1) {
            notification.setContentText("英语 | 专注中") //描述文字
                    .setOngoing(true) //不可滑动清除
        }
        else {
            notification.setContentText("英语 | 已暂停，点击通知继续") //描述文字
                    .setOngoing(false) //可滑动清除
                    .setContentIntent(resultPendingIntent) //跳转Activity
        }

        manager.notify(1, notification.build())
    }
}
