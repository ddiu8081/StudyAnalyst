package mr.study.analyst

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.act
import org.jetbrains.anko.toast
import android.app.NotificationManager
import android.os.Build
import android.app.NotificationChannel
import android.annotation.TargetApi
import android.content.Context


class MainActivity : AppCompatActivity() {

    data class TodoItem(val objectId: String, val name: String, val color: String, val plan: Int, val time: Int = 0)

    override fun onCreate(savedInstanceState: Bundle?) {

        val likeList:MutableList<TodoItem> = ArrayList ()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QMUIStatusBarHelper.translucent(this) //沉浸化状态栏
        QMUIStatusBarHelper.setStatusBarLightMode(act) //设置状态栏黑色字体图标
        view_statusBar.layoutParams.height = QMUIStatusBarHelper.getStatusbarHeight(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "timeRecorder"
            val channelName = "计时器"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.setShowBadge(true)
            val notificationManager = getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        recyclerView_todoList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView_todoList.adapter = ListAdapter(likeList) {
            val intent = Intent(act, TodoInfoActivity::class.java)
            startActivity(intent) //启动界面
        }

        likeList.add(TodoItem("1","政治","#E47C5D",60,30))
        likeList.add(TodoItem("2","数学","#CCBF82",26,40))
        likeList.add(TodoItem("3","阅读","#E8A0A2",135,90))
        likeList.add(TodoItem("4","英语","#E6CCA5",30))
        likeList.add(TodoItem("5","未分配","#D1D0D7",60))
        likeList.add(TodoItem("6","阅读","#CCBF82",90,50))
        likeList.add(TodoItem("7","历史","#9A8194",100,77))
        likeList.add(TodoItem("8","历史","#EBCC6E",120))
        likeList.add(TodoItem("9","历史","#D3B9A3",60,44))

        btn_analyse.setOnClickListener {
            val intent = Intent(act, AnalyseActivity::class.java)
            startActivity(intent) //启动界面
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
