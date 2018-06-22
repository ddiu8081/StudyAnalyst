package mr.study.analyst

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getDateToday() : String {
        val date = Date()
        val format_date = SimpleDateFormat("YYYY-MM-dd")
        return format_date.format(date)
    }
}