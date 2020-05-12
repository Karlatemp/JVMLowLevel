@file:Suppress("MemberVisibilityCanBePrivate")

package io.github.karlatemp.jll

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.SimpleRemapper
import org.objectweb.asm.tree.ClassNode
import sun.misc.Unsafe
import sun.reflect.ReflectionFactory
import java.lang.instrument.Instrumentation
import java.lang.reflect.Modifier
import java.util.function.Supplier

object ApplicationStartup {
    private val instrumentation: Instrumentation
    private val unsafe: Unsafe
    private val classReflectionDataOffset: Long

    init {
        instrumentation = Thread.currentThread().threadGroup.let {
            val count = it.activeCount()
            val array = Array<Thread?>(count) { null }
            it.enumerate(array)
            return@let array
        }.let {
            it.forEach { thread ->
                if (thread == null) return@forEach
                if (thread.name == "InstrumentationThread") {
                    if (thread is Supplier<*>) {
                        return@let thread.get() as Instrumentation
                    }
                }
            }
            error("Instrumentation not found! Please add -javaagent:JvmAgent.jar")
        }
        unsafe = Unsafe::class.java.getDeclaredField("theUnsafe").also { it.isAccessible = true }.get(null) as Unsafe
        unsafe.ensureClassInitialized(ClassNode::class.java)
        instrumentation.addTransformer { loader, className, classBeingRedefined, protectionDomain, classfileBuffer ->
            ClassReader(classfileBuffer).let { reader ->
                ClassNode().also {
                    reader.accept(it, 0)
                }
            }.let {
                if (it.name == "io/github/karlatemp/jll/ReflectionFactoryModel")
                    return@addTransformer classfileBuffer
                val writer = ClassWriter(0)
                it.accept(
                    ClassRemapper(
                        writer, SimpleRemapper(
                            "io/github/karlatemp/jll/ReflectionFactoryModel",
                            "sun/reflect/ReflectionFactory"
                        )
                    )
                )
                writer
            }.toByteArray()
        }
        classReflectionDataOffset = unsafe.objectFieldOffset(Class::class.java.getDeclaredField("reflectionData"))
    }

    fun makeReflectionFactoryModel() {
        println("package io.github.karlatemp.jll;")
        println("@SuppressWarnings({\"rawtypes\"})")
        println("public class ReflectionFactoryModel {")
        ReflectionFactory::class.java.declaredMethods.forEach {
            val access = it.modifiers
            if (it.name == "<clinit>" || it.name == "<init>") return@forEach
            if (Modifier.isPublic(access)) print("public ")
            if (Modifier.isPrivate(access)) print("private ")
            if (Modifier.isProtected(access)) print("protected ")
            if (Modifier.isStatic(access)) print("static ")
            if (Modifier.isFinal(access)) print("final ")
            print("native ")
            print(it.returnType.typeName)
            print(" ${it.name}(")
            it.parameterTypes.forEachIndexed { index, clazz ->
                if (index != 0) {
                    print(", ")
                }
                print(clazz.typeName)
                print(" var$index")
            }
            println(");")
        }
        println("}")
    }

    fun clearReflectionData() {
        instrumentation.allLoadedClasses.forEach {
            unsafe.putObjectVolatile(it, classReflectionDataOffset, null)
        }
    }

    private fun printHelloWorld() {
        println("Hello World")
    }

    fun initialize() {
        val of = unsafe.allocateInstance(OverrideReflectionFactory::class.java) as OverrideReflectionFactory
        if (!sun.reflect.ReflectionFactory::class.isInstance(of)) {
            error("Oops. This is not working!")
        }
        instrumentation.allLoadedClasses.forEach {
            kotlin.runCatching {
                it.declaredFields.forEach { field ->
                    if (Modifier.isStatic(field.modifiers)) {
                        if (field.type == ReflectionFactory::class.java) {
                            unsafe.putObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field), of)
                        }
                    }
                }
            }
        }
        clearReflectionData()
        instrumentation.addTransformer(MagicAccessorImplBlocking)
    }

    private object loader : ClassLoader() {
        fun defineClass(code: ByteArray): Class<*> {
            return defineClass(null, code, 0, code.size)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        initialize()
        testMagicAccessorImpl()
    }

    fun testMagicAccessorImpl() {
        val met = Runnable::class.java.getMethod("run").also { it.isAccessible = true }
        val runnable = Runnable { }
        repeat(50) {
            met.invoke(runnable)
        }
        ClassWriter(0).also {
            with(it) {
                visit(
                    Opcodes.V1_8, Opcodes.ACC_PUBLIC, "test", null, "sun/reflect/MethodAccessorImpl", arrayOf(
                        "java/lang/Runnable"
                    )
                )
                with(visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null)) {
                    visitLineNumber(0, Label().also { visitLabel(it) })
                    visitFieldInsn(Opcodes.ACC_PUBLIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    visitLdcInsn("HelloWorld")
                    visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "java/io/PrintStream",
                        "println",
                        "(Ljava/lang/String;)V",
                        false
                    )
                    visitInsn(Opcodes.RETURN)
                    visitMaxs(5, 5)
                }
            }
        }.toByteArray().let {
            loader.defineClass(it)
        }.let {
            unsafe.allocateInstance(it)
        }.let {
            it as Runnable
        }.run()
    }
}