package io.github.karlatemp.jll

import org.objectweb.asm.*
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import sun.misc.Unsafe
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandleInfo
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*

@Suppress("unused")
object LookupMapping {
    fun load() {
        javaClass.classLoader
    }

    private fun ClassNode.toByteCode(): ByteArray {
        return ClassWriter(0).also { accept(it) }.toByteArray()
    }

    private fun Unsafe.defineClass(code: ByteArray, loader: ClassLoader? = null): Class<*> {
        return defineClass(null, code, 0, code.size, loader, null)
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
        val realMapper = ClassNode().also {
            with(it) {
                visit(
                    Opcodes.V1_8, Opcodes.ACC_PUBLIC, "io/github/karlatemp/jll/${UUID.randomUUID()}",
                    null, "java/lang/Object", arrayOf(interfaces.name)
                )
                visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null).apply {
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
                    visitInsn(Opcodes.RETURN)
                    visitMaxs(3, 1)
                }
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
            realMapper.visitMethod(Opcodes.ACC_PUBLIC, it.name, Type.getMethodDescriptor(it), null, null).apply {
                var size = 1
                Type.getArgumentTypes(it).forEach { type ->
                    visitVarInsn(type.getOpcode(Opcodes.ILOAD), size)
                    size += type.size
                }
                visitMethodInsn(
                    Opcodes.INVOKESTATIC, "io/github/karlatemp/jll/LookupMapping",
                    it.name, Type.getMethodDescriptor(it), false
                )
                visitInsn(Type.getReturnType(it).also { a -> size += a.size }.getOpcode(Opcodes.IRETURN))
                visitMaxs(size, size)
            }
            node.visitMethod(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                it.name,
                Type.getMethodDescriptor(it),
                null,
                null
            ).apply {
                visitFieldInsn(Opcodes.GETSTATIC, node.name, "delegate", delegateType)
                var size = 0
                val types = Type.getArgumentTypes(it)
                types.forEach { type ->
                    visitVarInsn(type.getOpcode(Opcodes.ILOAD), size)
                    size += type.size
                }
                visitMethodInsn(
                    Opcodes.INVOKEINTERFACE,
                    interfaces.name,
                    it.name,
                    Type.getMethodDescriptor(it),
                    true
                )
                visitInsn(Type.getReturnType(it).also { a -> size += a.size }.getOpcode(Opcodes.IRETURN))
                visitMaxs(size, size)
            }
        }
        usf.defineClass(interfaces.toByteCode().save("interfaces"))
        val nodeKlass = usf.defineClass(node.toByteCode().save("node"))
        val bridge =
            usf.defineClass(realMapper.toByteCode().save("mapper"), LookupMapping::class.java.classLoader).newInstance()
        nodeKlass.getDeclaredField("delegate").apply {
            val base = usf.staticFieldBase(this)
            val offset = usf.staticFieldOffset(this)
            usf.putObject(base, offset, bridge)
        }
        ApplicationStartup.instrumentation.addTransformer { loader, _, _, _, classfileBuffer ->
            if (loader == null) return@addTransformer classfileBuffer
            val classNode = ClassReader(classfileBuffer).let { reader -> ClassNode().also { reader.accept(it, 0) } }
            classNode.methods.forEach { method ->
                method.instructions.forEach {
                    (it as? MethodInsnNode)?.apply {
                        if (owner == "java/lang/invoke/MethodHandles\$Lookup") {
                            owner = node.name
                            opcode = Opcodes.INVOKESTATIC
                            desc = desc.receiver()
                        }
                    }
                    (it as? InvokeDynamicInsnNode)?.apply {
                        with(bsm) {
                            if (owner == "java/lang/invoke/MethodHandles\$Lookup") {
                                bsm = Handle(
                                    Opcodes.H_INVOKESTATIC,
                                    node.name,
                                    name,
                                    desc.receiver(),
                                    isInterface
                                )
                            }
                        }
                    }
                }
            }
            return@addTransformer classNode.toByteCode()
        }
    }

    private fun ByteArray.save(name: String): ByteArray {
        // File("out/$name.class").writeBytes(this)
        return this
    }

    private fun String.receiver(): String =
        Type.getArgumentTypes(this).let { types ->
            Array(types.size + 1) { index ->
                if (index == 0) {
                    Type.getType("Ljava/lang/invoke/MethodHandles\$Lookup;")
                } else types[index - 1]
            }.let { types0 ->
                Type.getMethodDescriptor(
                    Type.getReturnType(this), *types0
                )
            }
        }

    @JvmStatic
    private fun check(klass: Class<*>) {
        UnsafeAccessConst.unsafe.forEach {
            if (it.isAssignableFrom(klass))
                throw IllegalAccessException()
        }
    }

    @JvmStatic
    fun findStaticSetter(receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: Class<*>): MethodHandle {
        check(refc)
        check(type)
        return receiver.findStaticSetter(refc, name, type)
    }

    @JvmStatic
    fun lookupClass(receiver: MethodHandles.Lookup): Class<*> {
        return receiver.lookupClass().also { check(it) }
    }

    @JvmStatic
    fun lookupModes(receiver: MethodHandles.Lookup): Int {
        return receiver.lookupModes()
    }

    @JvmStatic
    fun `in`(receiver: MethodHandles.Lookup, requested: Class<*>): MethodHandles.Lookup {
        check(requested)
        check(receiver.lookupClass())
        return receiver.`in`(requested)
    }

    @JvmStatic
    fun toString(receiver: MethodHandles.Lookup): String = receiver.toString()

    @JvmStatic
    fun findStatic(receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: MethodType): MethodHandle {
        check(receiver.lookupClass())
        check(refc)
        check(type.returnType())
        return receiver.findStatic(refc, name, type)
    }

    @JvmStatic
    fun findVirtual(receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: MethodType): MethodHandle {
        check(receiver.lookupClass())
        check(refc)
        check(type.returnType())
        return receiver.findVirtual(refc, name, type)
    }

    @JvmStatic
    fun findConstructor(receiver: MethodHandles.Lookup, refc: Class<*>, type: MethodType): MethodHandle {
        check(receiver.lookupClass())
        check(refc)
        return receiver.findConstructor(refc, type)
    }

    @JvmStatic
    fun findSpecial(
        receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: MethodType,
        specialCaller: Class<*>
    ): MethodHandle {
        check(refc)
        check(receiver.lookupClass())
        check(type.returnType())
        check(specialCaller)
        return receiver.findSpecial(refc, name, type, specialCaller)
    }

    @JvmStatic
    fun findGetter(receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: Class<*>): MethodHandle {
        check(receiver.lookupClass())
        check(refc)
        check(type)
        return receiver.findGetter(refc, name, type)
    }

    @JvmStatic
    fun findSetter(receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: Class<*>): MethodHandle {
        check(receiver.lookupClass())
        check(refc)
        check(type)
        return receiver.findSetter(refc, name, type)
    }

    @JvmStatic
    fun findStaticGetter(receiver: MethodHandles.Lookup, refc: Class<*>, name: String, type: Class<*>): MethodHandle {
        check(receiver.lookupClass())
        check(refc)
        check(type)
        return receiver.findStaticGetter(refc, name, type)
    }

    @JvmStatic
    fun bind(receiver: MethodHandles.Lookup, receiver0: Any, name: String, type: MethodType): MethodHandle {
        check(receiver.lookupClass())
        check(receiver0.javaClass)
        check(type.returnType())
        return receiver.bind(receiver0, name, type)
    }

    @JvmStatic
    fun unreflect(receiver: MethodHandles.Lookup, m: Method): MethodHandle {
        check(receiver.lookupClass())
        check(m.declaringClass)
        check(m.returnType)
        return receiver.unreflect(m)
    }

    @JvmStatic
    fun unreflectSpecial(receiver: MethodHandles.Lookup, m: Method, specialCaller: Class<*>): MethodHandle {
        check(receiver.lookupClass())
        check(m.declaringClass)
        check(m.returnType)
        check(specialCaller)
        return receiver.unreflectSpecial(m, specialCaller)
    }

    @JvmStatic
    fun unreflectConstructor(receiver: MethodHandles.Lookup, c: Constructor<*>): MethodHandle {
        check(receiver.lookupClass())
        check(c.declaringClass)
        return receiver.unreflectConstructor(c)
    }

    @JvmStatic
    fun unreflectGetter(receiver: MethodHandles.Lookup, f: Field): MethodHandle {
        check(receiver.lookupClass())
        check(f.declaringClass)
        check(f.type)
        return receiver.unreflectGetter(f)
    }

    @JvmStatic
    fun unreflectSetter(receiver: MethodHandles.Lookup, f: Field): MethodHandle {
        check(receiver.lookupClass())
        check(f.declaringClass)
        check(f.type)
        return receiver.unreflectSetter(f)
    }

    @JvmStatic
    fun revealDirect(receiver: MethodHandles.Lookup, target: MethodHandle): MethodHandleInfo {
        return receiver.revealDirect(target)
    }
}
