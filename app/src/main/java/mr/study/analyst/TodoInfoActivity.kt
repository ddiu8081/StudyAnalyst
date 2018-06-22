package mr.study.analyst

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_todo_info.*
import org.jetbrains.anko.act
import mr.study.analyst.R.id.progressBar
import android.R.attr.data
import android.graphics.Color
import android.support.v4.app.NotificationCompat.getExtras
import android.widget.TextView
import android.widget.ProgressBar
import mr.study.analyst.R.id.progressBar
import android.view.Window.FEATURE_NO_TITLE
import org.jetbrains.anko.toast
import org.litepal.LitePal

class TodoInfoActivity : AppCompatActivity() {

    private var planTimeFromMainActivity = 0
    private var timeToMainActivity = 0
    private var titleFromMainActivity = ""
    private var currntTimeFromMainActivity: Int = 0
    private var id : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_info)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
        view_statusBar.layoutParams.height = QMUIStatusBarHelper.getStatusbarHeight(this)

        id = intent.getLongExtra("id",0)

        titleFromMainActivity = "English"
        planTimeFromMainActivity = 48
        currntTimeFromMainActivity = 32

        btn_left.setOnClickListener {
            finish()
        }

        btn_right.setOnClickListener {
            val intent = Intent(act, TodoEditActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent) //启动界面
        }

//        textView_toast.setOnClickListener {
//            val intent1 = Intent(this, TimeRecordActivity::class.java)
//            val message = titleFromMainActivity+"#"+planTimeFromMainActivity+"_"+currntTimeFromMainActivity
//            intent1.putExtra("MESSAGE",message)
//            startActivityForResult(intent1,0)
//        }
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(requestCode==0 && resultCode==RESULT_OK) {
//            val bundle = data!!.getExtras()
//            var reText:String = ""
//            if(bundle != null)
//                reText = bundle.getString("second");
//            if(reText=="planSuccess"){
//                textView_todoMeta.setText("计划完成！");
//                var ratio = 100
//                progressBar.setProgress(ratio);
//            }
//            else{
//                textView_todoMeta.setText("已学习"+reText);
//                var hour = Integer.parseInt(reText.substring(0,2))
//                var minutes = Integer.parseInt(reText.substring(3,5))
//                var seconds = Integer.parseInt(reText.substring(6,8))
//                textView_todoMeta.setText("已学习"+minutes+"分钟")
//                var ratio = minutes*100/planTimeFromMainActivity
//                currntTimeFromMainActivity=minutes
//                progressBar.progress = ratio
//            }
//
//           // Log.d("text",text);
//           // editText.setText(text);
//        }
//    }

    override fun onStart() {
        super.onStart()

        val todoItem = LitePal.find(TodoItem::class.java,id)

        textView_todoName.text = todoItem.name
        view_todoColor.setBackgroundColor(Color.parseColor(todoItem.color))
        textView_todoMeta.text = "已学习" + todoItem.actuTime.toString() + "分钟"
        textView_todoPlan.text = todoItem.planTime.toString()
        textView_todoType.text = "计划"

        var progress:Int = todoItem.actuTime*100 / todoItem.planTime
        if (progress > 100) {
            progress = 100
        }
        progressBar.progress = progress

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
                    intent.putExtra("id",id)
                    startActivity(intent) //启动界面
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

            }
        }, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }
}
