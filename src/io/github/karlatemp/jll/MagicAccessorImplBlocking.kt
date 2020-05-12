package io.github.karlatemp.jll

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain

object MagicAccessorImplBlocking : ClassFileTransformer {
    private val DelegateClassLoader = Class.forName("sun.reflect.DelegatingClassLoader")
    private val MagicAccessorImpl = Class.forName("sun.reflect.MagicAccessorImpl")

    override fun transform(
        loader: ClassLoader?,
        className: String?,
        classBeingRedefined: Class<*>?,
        protectionDomain: ProtectionDomain?,
        classfileBuffer: ByteArray
    ): ByteArray {
        if (loader != null && loader.javaClass != DelegateClassLoader) {
            val node = ClassReader(classfileBuffer).let { reader ->
                ClassNode().also { reader.accept(it, 0) }
            }
            val superClass = node.superName ?: return classfileBuffer
            if (check(loader, superClass)) {
                node.methods.forEach {
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
                return ClassWriter(0).also { node.accept(it) }.toByteArray()
            }
        }
        return classfileBuffer
    }

    private fun check(loader: ClassLoader, klass: String): Boolean {
        val `class` = Class.forName(klass.replace('/', '.'), false, loader)
        return MagicAccessorImpl.isAssignableFrom(`class`)
    }
}