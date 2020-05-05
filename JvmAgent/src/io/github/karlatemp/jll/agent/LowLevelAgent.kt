package io.github.karlatemp.jll.agent

import java.lang.instrument.Instrumentation

object LowLevelAgent {
    @JvmStatic
    fun premain(arg: String?, instrumentation: Instrumentation) {
        InstrumentationThread(instrumentation)
    }
}