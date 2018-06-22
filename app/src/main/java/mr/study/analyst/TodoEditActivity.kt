package mr.study.analyst

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_todo_edit.*
import org.jetbrains.anko.act
import com.qmuiteam.qmui.util.QMUIDrawableHelper
import org.jetbrains.anko.toast
import org.litepal.LitePal
import android.support.v7.widget.LinearLayoutManager


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
            toast("已删除")
        }

        initList()

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
        editText_color.setText(thisItem.color.toString())
        view_todoColor.setBackgroundColor(Color.parseColor(thisItem.color))
    }

    fun save (view: View) {
        if (editText_name.text.toString() != "" && editText_planTime.text.toString() != "") {
            if (id < 1) {
                val item = TodoItem()
                item.name = editText_name.text.toString()
                item.color = editText_color.text.toString()
                item.planTime = editText_planTime.text.toString().toInt()
                item.save()
            }
            else {
                val item = LitePal.find(TodoItem::class.java, id)
                item.name = editText_name.text.toString()
                item.color = editText_color.text.toString()
                item.planTime = editText_planTime.text.toString().toInt()
                item.save()
            }
            finish()
        }
        else {
            toast("请填写完整！")
        }
    }

    private fun initList() {
        val colorList = listOf(
                "#A7BFA0",
                "#E47C5D",
                "#E8A0A2",
                "#C6CCA5",
                "#D1D0D7",
                "#CCBF82",
                "#9A8194",
                "#EBCC6E",
                "#D3B9A3"
        )

        recyclerView_colorList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        recyclerView_colorList.adapter = ColorListAdapter(colorList) {
            editText_color.setText(it)
            view_todoColor.setBackgroundColor(Color.parseColor(it))
        }
    }
}

