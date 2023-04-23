package com.zj.asm.hegui

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Date: 2021/12/17 11:02
 * @Desc:
 */
class OptimizedSysConfig(

    private val optimizedFileReaderClass: String = "com.replace.newobj.FileReaderProxy",

    private val optimizedAndroidIdClass: String = "com.sys.exchange.AndroidIdUtil",
    val sysHookPointList: List<SystemHookPoint> = systemHookPoints,
) : Serializable {

    val formatOptimizedAndroidIdClass: String
        get() = optimizedAndroidIdClass.replace(".", "/")

    val formatOptimizedFileReaderProxyClass: String
        get() = optimizedFileReaderClass.replace(".", "/")


}

private val systemHookPoints = listOf(
    SystemHookPoint(
        methodName = "getString"
    ),
)

class SystemHookPoint(
    val methodName: String
) : Serializable