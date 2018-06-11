package mr.study.analyst

import android.app.Notification.VISIBILITY_PUBLIC
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import com.github.mikephil.charting.charts.BarChart
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_analyse.*
import org.jetbrains.anko.act
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IAxisValueFormatter
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

        initBarChart(chart_timeline)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this,"timeRecorder")
        // Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0)
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
                        resultPendingIntent)

        manager.notify(1, notification.build())

    }

    private fun initBarChart(mChart: BarChart) {
        mChart.description.isEnabled = false
        mChart.setDrawBarShadow(false)
        mChart.setDrawGridBackground(false)
        mChart.setTouchEnabled(false) //禁止交互

        val xAxis = mChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false) //纵网格线
        xAxis.setDrawAxisLine(false) //横轴水平线
        xAxis.setDrawLabels(true) //横轴标签
        xAxis.textColor = Color.parseColor("#999999") //标签颜色

        mChart.axisLeft.setDrawGridLines(false) //横网格线
        mChart.axisLeft.isEnabled = false //左侧纵轴
        mChart.axisRight.isEnabled = false //右侧纵轴
        mChart.animateY(1000) //展示动画
        mChart.legend.isEnabled = false //图例
        mChart.setNoDataText("没有数据")

        setData(mChart)
    }

    private fun setData(mChart: BarChart) {
        val xVals = ArrayList<String>()
        val yVals = ArrayList<BarEntry>()
        val colorVals = ArrayList<Int>()

        for (i in 0..29) {
            val mult = 50f
            val value = (Math.random() * mult).toFloat() + mult / 3
            xVals.add((i + 1).toString() + "日")
            yVals.add(BarEntry(i.toFloat(), value))
            if (i > 3) {
                colorVals.add(resources.getColor(R.color.colorPrimary, null))
            }
            else {
                colorVals.add(resources.getColor(R.color.colorGray, null))
            }
        }

        val barDataSet = BarDataSet(yVals, "日学习量")
//        barDataSet.setColors(resources.getColor(R.color.colorPrimary,null))
        barDataSet.colors = colorVals
        barDataSet.setDrawValues(false)

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(barDataSet)

        val barData = BarData(dataSets)

        barData.barWidth = 0.5f
        mChart.data = barData
        mChart.setFitBars(true)

        val formatter = IAxisValueFormatter { value, _ -> xVals[value.toInt()] } //设置横坐标

        val xAxis = mChart.xAxis
        xAxis.granularity = 1f // minimum axis-step (interval) is 1
        xAxis.valueFormatter = formatter

        mChart.invalidate()
    }

}
