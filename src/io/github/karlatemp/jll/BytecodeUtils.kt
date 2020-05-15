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