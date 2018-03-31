package com.rhythm7.timepayment.plugin

import com.rhythm7.timepayment.core.annotations.TimePayment
import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter


/**
 * 文档： http://asm.ow2.org/asm50/javadoc/user/index.html
 * Created by Jaminchanks on 2018-03-31.
 */

class TimePaymentClassVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM5, classVisitor) {

    override fun visitMethod(access: Int, name: String?, desc: String?, signature: String?, exceptions: Array<String?>?): MethodVisitor {

        var methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        methodVisitor = object : AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc) {

            private var isInject = false

            override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor {
                println(">>>>>>>>>>>$desc")
                if (Type.getDescriptor(TimePayment::class.java) == desc) {
                    isInject = true
                }
                return super.visitAnnotation(desc, visible)
            }

            override fun onMethodEnter() {
                if (isInject) {
                    /**
                     * 访问方法指令
                    参数说明:
                    int opcode, 指令操作符
                    String owner, 方法拥有者:类名
                    String name, 方法名
                    String desc, 描述:返回值
                    boolean itf 方法拥有者是否接口
                     */
                    methodVisitor.visitFieldInsn(GETSTATIC, "com/rhythm7/timepayment/core/TimePaymentHelper",
                            "INSTANCE", "Lcom/rhythm7/timepayment/core/TimePaymentHelper;")
                    methodVisitor.visitLdcInsn(name)
                    methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime",
                            "()J", false)
                    methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/rhythm7/timepayment/core/TimePaymentHelper", "setStartTime",
                            "(Ljava/lang/String;J)V", false)
                }
            }

            override fun onMethodExit(opcode: Int) {
                if (isInject) {
                    methodVisitor.visitFieldInsn(GETSTATIC, "com/rhythm7/timepayment/core/TimePaymentHelper",
                            "INSTANCE", "Lcom/rhythm7/timepayment/core/TimePaymentHelper;")
                    methodVisitor.visitLdcInsn(name)
                    methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false)
                    methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/rhythm7/timepayment/core/TimePaymentHelper", "setEndTime", "(Ljava/lang/String;J)V", false)

                    methodVisitor.visitFieldInsn(GETSTATIC, "com/rhythm7/timepayment/core/TimePaymentHelper", "INSTANCE", "Lcom/rhythm7/timepayment/core/TimePaymentHelper;")
                    methodVisitor.visitLdcInsn(name)
                    methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/rhythm7/timepayment/core/TimePaymentHelper", "getPayTime", "(Ljava/lang/String;)V", false)
                }
            }
        }

        return methodVisitor
    }


}