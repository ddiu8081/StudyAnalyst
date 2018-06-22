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

    }

}
