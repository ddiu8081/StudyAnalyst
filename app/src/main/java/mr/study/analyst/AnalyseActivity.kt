package mr.study.analyst

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_analyse.*
import org.jetbrains.anko.act
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis


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
    }

    private fun initBarChart(mChart: BarChart) {
        mChart.description.isEnabled = false
//        mChart.setMaxVisibleValueCount(60)
        mChart.setPinchZoom(false)
        mChart.setDrawBarShadow(false)
        mChart.setDrawGridBackground(false)
        mChart.setTouchEnabled(false)

        val xAxis = mChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false) //纵网格线
        xAxis.setDrawAxisLine(false) //横轴水平线
        xAxis.setDrawLabels(true) //横轴标签
        xAxis.textColor = Color.parseColor("#999999")

        mChart.axisLeft.setDrawGridLines(false) //横网格线
        mChart.axisLeft.isEnabled = false //左侧纵轴
        mChart.axisRight.isEnabled = false //右侧纵轴
        mChart.animateY(1000) //展示动画
        mChart.legend.isEnabled = false //图例

        setData(mChart)
    }

    private fun setData(mChart: BarChart) {
        val yVal = ArrayList<BarEntry>()

        for (i in 0..29) {
            val mult = 50f
            val value = (Math.random() * mult).toFloat() + mult / 3
            yVal.add(BarEntry(i.toFloat(), value))
        }
        val set1: BarDataSet
        if (mChart.data != null && mChart.data.dataSetCount > 0) {
            set1 = mChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = yVal
            mChart.data.notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(yVal, "日学习量")
            set1.setColors(resources.getColor(R.color.colorPrimary,null))
            set1.setDrawValues(false)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.barWidth = 0.5f
            mChart.data = data
            mChart.setFitBars(true)
        }
        mChart.invalidate()
    }
}
