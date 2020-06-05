/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:26:46
 *
 * JVMLowLevel/JVM-LowLevel.accessor.accessor-jdk8.main/WrappedUnsafe.kt
 */

package io.github.karlatemp.jll.accessor.jdk8

import io.github.karlatemp.jll.accessor.UnsafeWrapper
import sun.misc.Unsafe
import java.lang.reflect.Field

object WrappedUnsafe : UnsafeWrapper {
    private val unsafe: Unsafe =
        Unsafe::class.java.getDeclaredField("theUnsafe")
            .also { it.isAccessible = true }
            .get(null) as Unsafe

    override fun staticFieldOffset(field: Field): Long {
        return unsafe.staticFieldOffset(field)
    }

    override fun staticFieldBase(field: Field): Any {
        return unsafe.staticFieldBase(field)
    }

    override fun putObject(base: Any, offset: Long, value: Any?) {
        unsafe.putObject(base, offset, value)
    }

    override fun ensureClassInitialized(klass: Class<*>) {
        unsafe.ensureClassInitialized(klass)
    }
}