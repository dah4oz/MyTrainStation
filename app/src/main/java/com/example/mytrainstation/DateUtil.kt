package com.example.mytrainstation

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {
        fun currentDateFormatted(): String {
            var now = Date()
            val formatter = SimpleDateFormat("dd MM yyyy")
            return formatter.format(now)
        }
    }
}