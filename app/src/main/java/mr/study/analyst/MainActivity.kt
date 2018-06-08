package mr.study.analyst

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    data class TodoItem(val objectId: String, val name: String, val color: String, val plan: Int)

    override fun onCreate(savedInstanceState: Bundle?) {

        val likeList:MutableList<TodoItem> = ArrayList ()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
        view_statusBar.layoutParams.height = QMUIStatusBarHelper.getStatusbarHeight(this)

        recyclerView_todoList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView_todoList.adapter = ListAdapter(likeList) {
            toast(it.objectId)
        }

        likeList.add(TodoItem("23456","政治","#E47C5D",60))
        likeList.add(TodoItem("234346","数学","#CCBF82",26))

    }
}
