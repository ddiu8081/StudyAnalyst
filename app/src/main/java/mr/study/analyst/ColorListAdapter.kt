package mr.study.analyst

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIDrawableHelper
import kotlinx.android.synthetic.main.color_list_item.view.*
import kotlinx.android.synthetic.main.todo_list_item.view.*


class ColorListAdapter(val items : List<String>, private val itemClickListener: (String)->Unit) : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.color_list_item, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(val view: View, private val itemClickListener: (String) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: String) {
            view.view_color.background = QMUIDrawableHelper.createDrawableWithSize(Resources.getSystem(),100,100,100,Color.parseColor(item))
            view.setOnClickListener {
                itemClickListener(item)
            }
        }
    }
}