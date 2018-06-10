package mr.study.analyst

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_time_line.*
import org.jetbrains.anko.act

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
    }
}
