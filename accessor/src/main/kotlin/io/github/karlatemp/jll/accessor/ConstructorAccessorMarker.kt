/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:11:43
 *
 * JVMLowLevel/JVM-LowLevel.accessor.main/ConstructorAccessorMarker.kt
 */

package io.github.karlatemp.jll.accessor

import java.lang.reflect.InvocationTargetException

/** This interface provides the declaration for
 * java.lang.reflect.Constructor.invoke(). Each Constructor object is
 * configured with a (possibly dynamically-generated) class which
 * implements this interface.  */
interface ConstructorAccessorMarker {
    /** Matches specification in [java.lang.reflect.Constructor]  */
    @Throws(
        InstantiationException::class,
        IllegalArgumentException::class,
        InvocationTargetException::class
    )
    fun newInstance(args: Array<Any?>?): Any?
}
