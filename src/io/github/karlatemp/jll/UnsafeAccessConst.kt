/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/17 13:48:43
 *
 * JVMLowLevel/JVMLowLevel/UnsafeAccessConst.kt
 */

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