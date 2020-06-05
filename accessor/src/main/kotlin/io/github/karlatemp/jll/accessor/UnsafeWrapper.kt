/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:12:49
 *
 * JVMLowLevel/JVM-LowLevel.accessor.main/UnsafeWrapper.kt
 */

package io.github.karlatemp.jll.accessor

import java.lang.reflect.Field

interface UnsafeWrapper {
    fun staticFieldOffset(field: Field): Long
    fun staticFieldBase(field: Field): Any
    fun putObject(base: Any, offset: Long, value: Any?)
    fun ensureClassInitialized(klass: Class<*>)
}