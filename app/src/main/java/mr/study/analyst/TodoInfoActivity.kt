package mr.study.analyst

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_todo_info.*
import org.jetbrains.anko.act

class TodoInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_info)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
        view_statusBar.layoutParams.height = QMUIStatusBarHelper.getStatusbarHeight(this)

        btn_left.setOnClickListener {
            finish()
        }

        btn_right.setOnClickListener {
            val intent = Intent(act, TodoEditActivity::class.java)
            startActivity(intent) //启动界面
        }
    }

    override fun onStart() {
        super.onStart()
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

                if(Math.abs(ax) < 0.5 && Math.abs(ay) < 0.5 && az < -9) {
                    // 反面朝上
                    sm.unregisterListener(this)
                    val intent = Intent(act, TimeRecordActivity::class.java)
                    startActivity(intent) //启动界面
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

            }
        }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }
}
