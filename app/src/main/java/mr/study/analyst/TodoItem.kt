package mr.study.analyst

import org.litepal.crud.LitePalSupport

class TodoItem : LitePalSupport() {
    var id: Long = 0
    var name: String? = null
    var color: String? = null
    var planTime: Int = 60
    var actuTime: Int = 0
}