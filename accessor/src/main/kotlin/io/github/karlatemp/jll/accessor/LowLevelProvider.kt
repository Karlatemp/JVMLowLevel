/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:30:29
 *
 * JVMLowLevel/JVM-LowLevel.accessor.main/LowLevelProvider.kt
 */

package io.github.karlatemp.jll.accessor

interface LowLevelProvider {
    val unsafe: UnsafeWrapper
}