/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:29:09
 *
 * JVMLowLevel/JVM-LowLevel.accessor.main/Accessors.kt
 */

package io.github.karlatemp.jll.accessor

object Accessors {
    val provider: LowLevelProvider

    init {
        val isJdk8 = kotlin.runCatching {
            Class.forName("java.lang.Module") // Java 9
        }.isFailure

        provider = Class.forName(
            "io.github.karlatemp.jll.accessor." +
                    (if (isJdk8) "jdk8" else "jdk9")
                    + ".AccessorProvider"
        ).let { klass ->
            val instance = klass.kotlin.objectInstance
            if (instance != null) return@let instance as LowLevelProvider
            return@let klass.newInstance() as LowLevelProvider
        }
    }
}