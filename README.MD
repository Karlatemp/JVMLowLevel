# JVMLowLevel

# 目标
- [ ] 反射重定向 | Redirect Reflection
  - [X] JDK 8
  - [ ] JDK 13
- [ ] MethodHandles 越权拦截 | Blocking MethodHandles
  - [X] JDK 8
  - [ ] JDK 13
- [ ] Unsafe 拦截 | Blocking Unsafe
- [ ] MagicAccessorImpl 拦截 | Blocking MagicAccessorImpl
  - [X] JDK 8
  - [ ] JDK 13

----

# Libraries used
- ASM 7.2
- Kotlin Java Runtime

# Realization

Pass `Instrumentation` to the main thread

Get `Unsafe` instance

```kotlin
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
```

## Redirect Reflection(JDK 8)
Make a model of `sun.reflect.ReflectionFactory`, like
[ReflectionFactoryModel](core/src/main/java/io/github/karlatemp/jll/ReflectionFactoryModel.java)

Then make a class to extend it. like
```kotlin
// Don't use object, it is not support
class MyCustomReflectionFactory private constructor() : ReflectionFactoryModel() {
}
```

So, we make a custom factory. It `extends ReflectionFactoryModel`,
NOT `extends sun.reflect.ReflectionFactory`. We need to make the
JVM think that this class `extends ReflectionFactory`.

We use `Instrumentation` to edit the bytecode.

```kotlin
unsafe.ensureClassInitialized(ClassNode::class.java) // Preload ASM
instrumentation.addTransformer { loader, className, classBeingRedefined, protectionDomain, classfileBuffer ->
    ClassReader(classfileBuffer).let { reader ->
        ClassNode().also {
            reader.accept(it, 0)
        }
    }.let {
        // If the class is the model, ignore it
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
```

Then we need to alloc it. We CANNOT use `new MyCustomReflectionFacotry()`.
Because the constructor of `sun.reflet.ReflectionFactory` is private.

We use `sun.misc.Unsafe` to alloc our custom reflection factory.
```kotlin
val customReflectionFactory = unsafe.allocateInstance(MyCustomReflectionFactory::class.java) as MyCustomReflectionFactory
if(!sun.reflect.ReflectionFactory::class.isInstance(customReflectionFactory)) {
    error("Oops. This is not working!")
}
```

Override jvm default reflection factory.
We search all loaded classes to ensure that there are no missing fields.

```kotlin
instrumentation.allLoadedClasses.forEach {
    kotlin.runCatching {
        it.declaredFields.forEach { field ->
            if (Modifier.isStatic(field.modifiers)) {
                if (field.type == ReflectionFactory::class.java) {
                    unsafe.putObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field), customReflectionFactory)
                }
            }
        }
    }
}
```

Then, the most important step. Clean all the reflection data.

```kotlin
val classReflectionDataOffset = unsafe.objectFieldOffset(Class::class.java.getDeclaredField("reflectionData"))
instrumentation.allLoadedClasses.forEach {
    unsafe.putObjectVolatile(it, classReflectionDataOffset, null)
}
```

We have done these steps, but we have to confirm whether they work or not.

```kotlin
// Don't use object, it is not support
class MyCustomReflectionFactory private constructor() : ReflectionFactoryModel() {
    override fun newMethodAccessor(var0: Method): MethodAccessor {
        println(var0)
        return super.newMethodAccessor(var0)
    }
}
```
Then run
```kotlin
Runnable::class.java.getDeclaredMethod("run").invoke(Runnable {
    println("Hello")
})
```

If there are no surprises, you will see the following log
```text
public abstract void java.lang.Runnable.run()
Hello

```

---

# LICENSE

The license for this project is **`GNU AFFERO GENERAL PUBLIC LICENSE version 3`**
