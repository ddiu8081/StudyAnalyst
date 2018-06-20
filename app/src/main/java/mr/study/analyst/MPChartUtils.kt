package mr.study.analyst

import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.util.*

object MPChartUtils {
    fun initBarChart(context: Context, mChart: BarChart) {
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

        setData(context, mChart)
    }

    fun setData(context: Context, mChart: BarChart) {
        val xVals = ArrayList<String>()
        val yVals = ArrayList<BarEntry>()
        val colorVals = ArrayList<Int>()

        for (i in 0..29) {
            val mult = 50f
            val value = (Math.random() * mult).toFloat() + mult / 3
            xVals.add((i + 1).toString() + "日")
            yVals.add(BarEntry(i.toFloat(), value))
            if (i > 3) {
                colorVals.add(context.resources.getColor(R.color.colorPrimary, null))

            }
            else {
                colorVals.add(context.resources.getColor(R.color.colorGray, null))
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