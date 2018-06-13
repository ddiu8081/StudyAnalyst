package mr.study.analyst

import android.app.Service
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_time_line.*
import org.jetbrains.anko.act
import android.hardware.SensorManager
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator


class TimeLineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
        view_statusBar.layoutParams.height = QMUIStatusBarHelper.getStatusbarHeight(this)

        btn_left.setOnClickListener {
            finish()
        }

        var vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

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

                textView_analyse_title.text = "x:" + ax.toString() + " y:" + ay.toString() + " z:" + az.toString()
                if(Math.abs(ax) < 0.5 && Math.abs(ay) < 0.5 && az < -9) {
                    textView_analyse_title.text = "反面朝上！"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                    else {
                        vibrator.vibrate(500)
                    }
                }
                else if(Math.abs(ax) < 0.5 && Math.abs(ay) < 0.5 && az > 9) {
                    textView_analyse_title.text = "正面朝上！"
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

            }
        }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }
}
