package com.friendspharma.app.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object Utils {

    private val bengaliDayName: HashMap<Int, String> = hashMapOf(
        6 to "শনিবার", 7 to "রবিবার", 1 to "সোমবার", 2 to "মঙ্গলবার", 3 to "বুধবার",
        4 to "বৃহস্পতিবার", 5 to "শুক্রবার"
    )

    private val bengaliMonthName: HashMap<Int, String> = hashMapOf(
        1 to "জানুয়ারি", 2 to "ফেব্রুয়ারি", 3 to "মার্চ", 4 to "এপ্রিল", 5 to "মে",
        6 to "জুন", 7 to "জুলাই", 8 to "আগস্ট", 9 to "সেপ্টেম্বর", 10 to "অক্টোবর",
        11 to "নভেম্বর", 12 to "ডিসেম্বর"
    )

    private val bengaliNumberChar: HashMap<Char, String> = hashMapOf(
        '1' to "১", '2' to "২", '3' to "৩", '4' to "৪", '5' to "৫",
        '6' to "৬", '7' to "৭", '8' to "৮", '9' to "৯", '0' to "০"
    )

    private fun bengaliNumberString(number: String): String {
        var bangla = ""
        for (ch in number) {
            bangla += bengaliNumberChar[ch]
        }
        return bangla
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getBengaliDate(diff: Int = 0): String {
        val now = LocalDateTime.now().plusDays(diff.toLong())
        return bengaliDayName[now.dayOfWeek.value] + ", " + bengaliMonthName[now.monthValue] + " " + bengaliNumberString(
            now.dayOfMonth.toString()
        ) + ", " + bengaliNumberString(now.year.toString())
    }

}