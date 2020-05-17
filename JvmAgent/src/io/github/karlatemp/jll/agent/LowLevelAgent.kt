/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/17 13:48:43
 *
 * JVMLowLevel/JvmAgent/LowLevelAgent.kt
 */

package io.github.karlatemp.jll.agent

import java.lang.instrument.Instrumentation

object LowLevelAgent {
    @JvmStatic
    fun premain(arg: String?, instrumentation: Instrumentation) {
        InstrumentationThread(instrumentation)
    }
}