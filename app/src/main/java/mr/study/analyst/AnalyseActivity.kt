package mr.study.analyst

import android.app.Notification.VISIBILITY_PUBLIC
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_analyse.*
import org.jetbrains.anko.act
import android.app.NotificationManager
import android.content.Context
import android.app.PendingIntent
import android.util.Log
import org.jetbrains.anko.toast
import org.litepal.LitePal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class AnalyseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyse)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
        view_statusBar.layoutParams.height = QMUIStatusBarHelper.getStatusbarHeight(this)

        btn_left.setOnClickListener {
            finish()
        }

//        view_timeline.setOnClickListener {
//            val intent = Intent(act, TimeLineActivity::class.java)
//            startActivity(intent) //启动界面
//        }

        MPChartUtils.initBarChart(this, chart_timeline)

        initData()

    }

    private fun initData() {
        // 今日专注
        var acts = LitePal.findAll(ActivityItem::class.java)
        val daysSet = HashSet<String>()
        var allFocusMin = 0
        var todayFocusMin = 0
        for (act in acts) {
            allFocusMin += act.duringTime
            daysSet.add(act.date)
            if (act.date == DateUtils.getDateToday()) {
                todayFocusMin += act.duringTime
            }
        }

        textView_days.text = daysSet.size.toString() //记录天数
        if (allFocusMin <= 60) {
            textView_allFocus.text = allFocusMin.toString() + "分" //总专注
        }
        else {
            val df = DecimalFormat("#.#")
            val allFocusHour = df.format(allFocusMin.toFloat() / 60)
            textView_allFocus.text = allFocusHour.toString() + "时" //总专注
        }

        textView_todayFocus.text = todayFocusMin.toString() + "分" //今日专注
    }

}
