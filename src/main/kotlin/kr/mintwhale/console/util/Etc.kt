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

    fun checkNumber(value: String) : Boolean {
        return checkRegex(value, "[^\\d]")
    }

    fun checkMobile(value: String) : Boolean {
        return checkRegex(value, "^\\d{3}-\\d{3,4}-\\d{4}\$")
    }

    fun checkTelnum(value: String) : Boolean {
        return checkRegex(value, "^\\d{2,3}-\\d{3,4}-\\d{4}\$")
    }

    fun checkPassword(value: String) : Boolean {
        return checkRegex(value, "(?=.*\\d{1,20})(?=.*[~`!@#\$%\\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,20}).{8,20}\$")
    }

    fun checkEmail(value: String) : Boolean {
        return checkRegex(value, "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}\$")
    }

    fun checkRegex(value: String, regex: String): Boolean {
        return value.matches(regex.toRegex())
    }
}