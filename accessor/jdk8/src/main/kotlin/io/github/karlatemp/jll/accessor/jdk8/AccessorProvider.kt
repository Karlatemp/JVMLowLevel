/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:26:34
 *
 * JVMLowLevel/JVM-LowLevel.accessor.accessor-jdk8.main/AccessorLowLevelProvider.kt
 */

package io.github.karlatemp.jll.accessor.jdk8

import io.github.karlatemp.jll.accessor.LowLevelProvider
import io.github.karlatemp.jll.accessor.UnsafeWrapper

object AccessorProvider : LowLevelProvider {
    override val unsafe: UnsafeWrapper = WrappedUnsafe
}
