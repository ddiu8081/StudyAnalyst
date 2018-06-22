package mr.study.analyst

import org.litepal.crud.LitePalSupport
import java.util.*

class ActivityItem : LitePalSupport() {
    var id: Long = 0
    var name: String? = null
    var duringTime: Int = 0
    var date: String = DateUtils.getDateToday()
}