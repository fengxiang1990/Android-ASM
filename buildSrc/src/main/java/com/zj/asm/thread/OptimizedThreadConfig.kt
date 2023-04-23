package com.zj.asm.thread

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Date: 2021/12/17 11:02
 * @Desc:
 */
class OptimizedThreadConfig(
    private val optimizedThreadClass: String = "com.zj.android_asm.AsmThread",
    private val optimizedThreadPoolClass: String = "com.zj.android_asm.AsmExecutors",
    val threadHookPointList: List<ThreadHookPoint> = threadHookPoints,
) : Serializable {

    val formatOptimizedThreadClass: String
        get() = optimizedThreadClass.replace(".", "/")

    val formatOptimizedThreadPoolClass: String
        get() = optimizedThreadPoolClass.replace(".", "/")

}

private val threadHookPoints = listOf(
    ThreadHookPoint(
        methodName = "newFixedThreadPool"
    ),
    ThreadHookPoint(
        methodName = "newSingleThreadExecutor"
    ),
    ThreadHookPoint(
        methodName = "newCachedThreadPool"
    ),
    ThreadHookPoint(
        methodName = "newSingleThreadScheduledExecutor"
    ),
    ThreadHookPoint(
        methodName = "newScheduledThreadPool"
    ),
)

class ThreadHookPoint(
    val methodName: String
) : Serializable