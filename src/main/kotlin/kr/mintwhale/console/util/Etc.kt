package kr.mintwhale.console.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Etc {
    fun setComma(value: Int) : String {
        val nf = NumberFormat.getInstance()
        return nf.format(value)
    }

    fun setStringtoDate(value: String, ext : String = "yyyy-MM-dd HH:mm:ss") : Date {
        val sf = SimpleDateFormat(ext)
        return sf.parse(value)
    }

    fun setDatetoString(value: Date, ext : String = "yyyy-MM-dd HH:mm:ss") : String {
        val sf = SimpleDateFormat(ext)
        return sf.format(value)
    }

    fun randomRange(n1: Int, n2: Int): Int {
        return (Math.random() * (n2 - n1 + 1)).toInt() + n1
    }

    fun setDateAdd(type: Int, add: Int, defdate: Date = Date()) : Date {
        val cal = Calendar.getInstance()
        cal.time = defdate
        cal.add(type, add)
        return Date(cal.timeInMillis)
    }
}