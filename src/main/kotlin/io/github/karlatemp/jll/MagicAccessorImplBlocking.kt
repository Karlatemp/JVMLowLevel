/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/17 13:48:43
 *
 * JVMLowLevel/JVMLowLevel/MagicAccessorImplBlocking.kt
 */

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
                node.makeVerityError()
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