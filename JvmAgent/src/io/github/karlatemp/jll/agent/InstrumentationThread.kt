/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/17 13:48:43
 *
 * JVMLowLevel/JvmAgent/InstrumentationThread.kt
 */

package io.github.karlatemp.jll.agent

import java.lang.instrument.Instrumentation
import java.util.function.Supplier

class InstrumentationThread(private var ins: Instrumentation?) :
    Thread("InstrumentationThread"), Supplier<Any?> {
    private var running: Boolean = true
    private var started: Boolean = false

    @Suppress("unused")
    fun instrumentationThread() {
    }

    override fun get(): Any? {
        val i = ins
        ins = null
        running = false
        return i
    }

    init {
        requireNotNull(ins)
        isDaemon = true
        start()
        @Suppress("ControlFlowWithEmptyBody")
        while (!started) {
        }
    }

    override fun run() {
        started = true
        @Suppress("ControlFlowWithEmptyBody")
        while (running) {
        }
    }
}