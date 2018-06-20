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

        view_timeline.setOnClickListener {
            val intent = Intent(act, TimeLineActivity::class.java)
            startActivity(intent) //启动界面
        }

        MPChartUtils.initBarChart(this, chart_timeline)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this,"timeRecorder")

        // Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0)
        val pauseIntent = Intent(this, TimeRecordActivity::class.java)
        val pausePendingIntent = PendingIntent.getActivity(this, 0, pauseIntent, 0)

        val notification = builder
                .setContentTitle("正在专注") //标题
                .setContentText("英语 | 已专注 14 分钟") //描述文字
                .setSmallIcon(R.drawable.ic_stat_name) //通知栏图标
                .setShowWhen(false) //显示时间
//                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)) //大图标
                .setColor(resources.getColor(R.color.colorPrimary, null)) //颜色
                .setContentIntent(resultPendingIntent) //跳转Activity
                .setAutoCancel(false) //进入应用自动消失
                .setOngoing(true) //不可滑动清除
                .setVisibility(VISIBILITY_PUBLIC)
                .setUsesChronometer(true)
                .setNumber(1)
                .addAction(R.drawable.ic_stat_name, "暂停",
                        pausePendingIntent)

        manager.notify(1, notification.build())

    }



}
