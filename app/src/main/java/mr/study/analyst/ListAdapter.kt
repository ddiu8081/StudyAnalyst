package mr.study.analyst

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import kotlinx.android.synthetic.main.todo_list_item.view.*


class ListAdapter(val items : List<MainActivity.TodoItem>, private val itemClickListener: (MainActivity.TodoItem)->Unit) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(val view: View, private val itemClickListener: (MainActivity.TodoItem) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: MainActivity.TodoItem) {

            var progress:Float = item.time.toFloat() / item.plan
            if (progress > 1) {
                progress = 1f
            }
            val progressWidth = ((QMUIDisplayHelper.getScreenWidth(view.context) - QMUIDisplayHelper.dp2px(view.context,60)) * progress).toInt()
            view.textView_todoName.text = item.name
            view.textView_todoMeta.text = "已专注 " + item.time + " 分钟"
            view.textView_todoPlan.text = item.plan.toString()
            view.view_todoColor.setBackgroundColor(Color.parseColor(item.color))
            view.view_todoProgress.layoutParams.width = progressWidth
            view.setOnClickListener {
                itemClickListener(item)
            }
        }
    }
}