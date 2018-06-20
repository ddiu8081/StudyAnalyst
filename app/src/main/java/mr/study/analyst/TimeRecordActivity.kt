package mr.study.analyst

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import org.jetbrains.anko.act
import android.os.SystemClock
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

    }

    val handler = Handler()
    override fun run() {
        recLen++
        textView_timer.text = recLen.toString()
        handler.postDelayed(this,1000)
    }
}
