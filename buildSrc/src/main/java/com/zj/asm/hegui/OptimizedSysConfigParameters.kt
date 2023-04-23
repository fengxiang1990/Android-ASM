package com.zj.asm.hegui

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.io.FileReader

/**
 * @Author: leavesCZY
 * @Date: 2021/12/16 15:11
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
interface OptimizedSysConfigParameters : InstrumentationParameters {
    @get:Input
    val config: Property<OptimizedSysConfig>
}

abstract class OptimizedSysClassVisitorFactory :
    AsmClassVisitorFactory<OptimizedSysConfigParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return OptimizedSysClassVisitor(
            config = parameters.get().config.get(),
            nextClassVisitor = nextClassVisitor
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return classData.className.contains("com.zj.android_asm")
    }

}

private class OptimizedSysClassVisitor(
    private val config: OptimizedSysConfig,
    private val nextClassVisitor: ClassVisitor
) :
    ClassNode(Opcodes.ASM5) {

    companion object {

        private const val settingsClass = "android/provider/Settings${'$'}Secure"

        private const val fileReaderClass = "java/io/FileReader"
    }

    override fun visitEnd() {
        super.visitEnd()
        methods.forEach { methodNode ->
            val instructions = methodNode.instructions
            if (instructions != null && instructions.size() > 0) {
                instructions.forEach { instruction ->
                    when (instruction.opcode) {
                        Opcodes.INVOKESTATIC -> {
                            val methodInsnNode = instruction as? MethodInsnNode
                            println("fxa owner->"+methodInsnNode?.owner)
                            if (methodInsnNode?.owner == settingsClass) {
                                transformInvokeExecutorsInstruction(
                                    methodNode,
                                    instruction
                                )
                            }
                        }
                        // new 的时候替换对象
                        Opcodes.NEW -> {
                            val typeInsnNode = instruction as? TypeInsnNode
                            if (typeInsnNode != null) {
                                if (typeInsnNode.desc == fileReaderClass) {
                                    transformNewFileReaderInstruction(methodNode,typeInsnNode);
                                }
                            }
                        }
                    }
                }
            }
        }
        accept(nextClassVisitor)
    }

    private fun transformInvokeExecutorsInstruction(
        methodNode: MethodNode,
        methodInsnNode: MethodInsnNode
    ) {
        val pointMethod = config.sysHookPointList.find { it.methodName == methodInsnNode.name }
        println("fxa pointMethod->$pointMethod")
        if (pointMethod != null) {
            //将 Executors 替换为 OptimizedThreadPool
            println("fxa owner old->${methodInsnNode.owner}")
            methodInsnNode.owner = config.formatOptimizedAndroidIdClass
            println("fxa owner new->${methodInsnNode.owner}")
            //为调用 newFixedThreadPool 等方法的指令多插入一个 String 类型的方法入参参数声明
            methodInsnNode.insertArgument(String::class.java)
            //将 className 作为上述 String 参数的入参参数
            methodNode.instructions.insertBefore(methodInsnNode, LdcInsnNode(simpleClassName))
        }
    }

    private fun transformNewFileReaderInstruction(
        methodNode: MethodNode,
        typeInsnNode: TypeInsnNode
    ) {
        val instructions = methodNode.instructions
        val typeInsnNodeIndex = instructions.indexOf(typeInsnNode)
        //从 typeInsnNode 指令开始遍历，找到调用 FileReader 构造函数的指令，然后对其进行替换
        for (index in typeInsnNodeIndex + 1 until instructions.size()) {
            val node = instructions[index]
            if (node is MethodInsnNode && node.isThreadInitMethod()) {
                //将 FileReader 替换为 FileReaderProxy
                typeInsnNode.desc = config.formatOptimizedFileReaderProxyClass
                node.owner = config.formatOptimizedFileReaderProxyClass
                //为调用 FileReader 构造函数的指令多插入一个 String 类型的方法入参参数声明
                node.insertArgument(String::class.java)
                //将 ClassName 作为构造参数传给 FileReaderProxy
                instructions.insertBefore(node, LdcInsnNode(simpleClassName))
                break
            }
        }
    }

    private fun MethodInsnNode.isThreadInitMethod(): Boolean {
        return this.owner == fileReaderClass && this.name == "<init>"
    }


}