package io.github.karlatemp.jll

import sun.misc.Unsafe
import sun.reflect.ReflectionFactory
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles

object UnsafeAccessConst {
    @JvmField
    val unsafe = arrayOf(
        MethodHandles::class.java,
        MethodHandles.Lookup::class.java,
        Unsafe::class.java,
        ReflectionFactory::class.java,
        MethodHandle::class.java
    )
}