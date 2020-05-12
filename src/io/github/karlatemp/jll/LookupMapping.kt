package io.github.karlatemp.jll

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import sun.misc.Unsafe
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.Modifier
import java.util.*

@Suppress("unused")
object LookupMapping {
    private fun ClassNode.toByteCode(): ByteArray {
        return ClassWriter(0).also { accept(it) }.toByteArray()
    }

    private fun Unsafe.defineClass(code: ByteArray): Class<*> {
        return defineClass(null, code, 0, code.size, null, null)
    }

    init {
        val usf = ApplicationStartup.unsafe
        val mapper = LookupMapping::class.java
        val interfaces = ClassNode().also {
            with(it) {
                visit(
                    Opcodes.V1_8,
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_INTERFACE,
                    "java/lang/invoke/MethodHandler\$Lookup\$${UUID.randomUUID()}",
                    null,
                    "java/lang/Object",
                    null
                )
            }
        }
        val node = ClassNode().also {
            with(it) {
                visit(
                    Opcodes.V1_8,
                    Opcodes.ACC_PUBLIC,
                    "java/lang/invoke/MethodHandler\$Lookup\$${UUID.randomUUID()}",
                    null,
                    "java/lang/Object",
                    null
                )
                visitField(
                    Opcodes.ACC_PRIVATE or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC,
                    "delegate", "L${interfaces.name};", null, null
                )
            }
        }
        val delegateType = "L${interfaces.name};"
        mapper.methods.filter { Modifier.isStatic(it.modifiers) }.filter {
            it.parameterCount > 0
        }.filter {
            it.parameterTypes[0] == MethodHandles.Lookup::class.java
        }.forEach {
            interfaces.visitMethod(Opcodes.ACC_PUBLIC, it.name, Type.getMethodDescriptor(it), null, null)
            node.visitMethod(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                it.name,
                Type.getMethodDescriptor(it),
                null,
                null
            ).apply {
                visitFieldInsn(Opcodes.GETSTATIC, node.name, "delegate", delegateType)
                var size = 1
                val types = Type.getArgumentTypes(it)
                types.forEach { type ->
                    visitVarInsn(type.getOpcode(Opcodes.ILOAD), size)
                    size += type.size
                }
                visitInsn(Type.getReturnType(it).also { size += it.size }.getOpcode(Opcodes.IRETURN))
                visitMaxs(size, size)
            }
        }
        println(usf.defineClass(interfaces.toByteCode()))
        println(usf.defineClass(node.toByteCode()))
    }

    @JvmStatic
    fun findStaticSetter(receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: Class<*>): MethodHandle {
        return receiver.findStaticSetter(refc, name, type)
    }

    @JvmStatic
    fun findVirtual(receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: MethodType): MethodHandle {
        return receiver.findVirtual(refc, name, type)
    }

}