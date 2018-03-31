package com.rhythm7.timepayment.core

import java.util.*

/**
 * Created by Jaminchanks on 2018-03-31.
 */

object TimePaymentHelper {

    private val startTimeMap by lazy { HashMap<String, Long?>() }
    private val endTimeMap by lazy { HashMap<String, Long?>() }

    fun setStartTime(methodName: String, time: Long) { startTimeMap[methodName] = time }

    fun setEndTime(methodName: String, time: Long) { endTimeMap[methodName] = time }

    fun getCostTime(methodName: String) {
        System.nanoTime()

        val costTime = endTimeMap[methodName]?:0 - (startTimeMap[methodName]?:0)
        println("""

            >>>========================================================>>>
                            方法$methodName(...):
                                >>>耗时${costTime / 1000_000_000} 毫秒
            >>>========================================================>>>
            """)
    }
}