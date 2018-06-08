package mr.study.analyst

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            view.textView_todoName.text = item.name
            view.view_todoColor.setBackgroundColor(Color.parseColor(item.color))
            view.textView_todoPlan.text = item.plan.toString()
            view.setOnClickListener {
                itemClickListener(item)
            }
        }
    }
}