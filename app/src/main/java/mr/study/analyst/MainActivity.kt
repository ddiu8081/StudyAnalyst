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

    data class TodoItem(val objectId: String, val name: String, val color: String, val plan: Int)

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
            toast(it.objectId)
        }

        likeList.add(TodoItem("23456","政治","#E47C5D",60))
        likeList.add(TodoItem("234346","数学","#CCBF82",26))

        btn_analyse.setOnClickListener {
            val intent = Intent(act, AnalyseActivity::class.java)
//            intent.putExtra("data","This is from MainActivity.")
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
