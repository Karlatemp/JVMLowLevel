/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:08:36
 *
 * JVMLowLevel/JVM-LowLevel.accessor.main/MethodAccessorMarker.kt
 */

package io.github.karlatemp.jll.accessor

import java.lang.reflect.InvocationTargetException

interface MethodAccessorMarker {
    @Throws(IllegalArgumentException::class, InvocationTargetException::class)
    operator fun invoke(thiz: Any?, args: Array<Any?>?): Any?
}
