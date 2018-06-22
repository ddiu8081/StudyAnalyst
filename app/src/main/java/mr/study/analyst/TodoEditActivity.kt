package mr.study.analyst

import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_todo_edit.*
import org.jetbrains.anko.act
import android.widget.TextView
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import android.view.LayoutInflater
import org.jetbrains.anko.toast
import org.litepal.LitePal


class TodoEditActivity : AppCompatActivity() {

    private var id : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_edit)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
        view_statusBar.layoutParams.height = QMUIStatusBarHelper.getStatusbarHeight(this)

        id = intent.getLongExtra("id",0)

        btn_left.setOnClickListener {
            finish()
        }

        btn_right.setOnClickListener {
            LitePal.delete(TodoItem::class.java,id)
            val intent = Intent(act, MainActivity::class.java)
            intent.putExtra("id",it.id)
            startActivity(intent) //启动界面
        }

        if (id < 1) {
            btn_right.visibility = View.INVISIBLE
        }
        else {
            initData(id)
        }
    }

    private fun initData(id: Long) {
        val thisItem = LitePal.find(TodoItem::class.java, id)
        editText_name.setText(thisItem.name.toString())
        editText_planTime.setText(thisItem.planTime.toString())
    }

    fun save (view: View) {
        if (id < 1) {
            val item = TodoItem()
            item.name = editText_name.text.toString()
            item.color = "#E4D05D"
            item.planTime = editText_planTime.text.toString().toInt()
            item.save()
        }
        else {
            val item = LitePal.find(TodoItem::class.java, id)
            item.name = editText_name.text.toString()
            item.planTime = editText_planTime.text.toString().toInt()
            item.save()
        }
        finish()
    }
}

