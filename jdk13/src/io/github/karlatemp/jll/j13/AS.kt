package io.github.karlatemp.jll.j13

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.SimpleRemapper
import org.objectweb.asm.tree.ClassNode
import java.lang.instrument.ClassFileTransformer
import java.lang.instrument.Instrumentation
import java.lang.reflect.Modifier
import java.security.ProtectionDomain
import java.util.function.Supplier

object AS {
    @JvmField
    val instrumentation: Instrumentation

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
        val module = AS::class.java.module
        instrumentation.redefineModule(
            Object::class.java.module,
            setOf(),
            mapOf(
                "jdk.internal.reflect" to setOf(module),
                "jdk.internal.access" to setOf(module),
                "jdk.internal.misc" to setOf(module)
            ),
            mapOf(),
            setOf(),
            mapOf()
        )
    }

    @JvmStatic
    fun makeReflectionFactoryModule() {
        println("package io.github.karlatemp.jll;")
        println("@SuppressWarnings({\"rawtypes\"})")
        println("public class ReflectionFactoryModule {")
        Class.forName("jdk.internal.reflect.ReflectionFactory").declaredMethods.forEach {
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

    @JvmStatic
    fun createAccessor() {
        instrumentation.addTransformer(object : ClassFileTransformer {
            override fun transform(
                loader: ClassLoader,
                className: String,
                classBeingRedefined: Class<*>,
                protectionDomain: ProtectionDomain,
                classfileBuffer: ByteArray
            ): ByteArray {
                println("wwww")
                return ClassReader(classfileBuffer).let { reader ->
                    ClassNode().also {
                        reader.accept(it, 0)
                    }
                }.let {
                    if (it.name == "io/github/karlatemp/jll/j13/OverrideReflectionFactory") {
                        val writer = ClassWriter(0)
                        it.accept(
                            ClassRemapper(
                                writer, SimpleRemapper(
                                    "io/github/karlatemp/jll/j13/ReflectionModel",
                                    "jdk/internal/reflect/ReflectionFactory"
                                )
                            )
                        )
                        writer
                    } else return@transform classfileBuffer
                }.toByteArray()
            }
        })
    }
}