/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/17 13:48:43
 *
 * JVMLowLevel/JVMLowLevel/BytecodeUtils.kt
 */

package io.github.karlatemp.jll

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

fun ClassNode.makeVerityError() {
    methods.forEach {
        it.instructions.clear()
        it.visitTypeInsn(Opcodes.NEW, "java/lang/VerifyError")
        it.visitInsn(Opcodes.DUP)
        it.visitLdcInsn("Cannot access MagicAccessorImpl out of DelegatingClassLoader")
        it.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/lang/VerifyError",
            "<init>",
            "(Ljava/lang/String;)V",
            false
        )
        it.visitInsn(Opcodes.ATHROW)
    }
}