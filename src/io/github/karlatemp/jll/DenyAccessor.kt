package io.github.karlatemp.jll

import sun.reflect.FieldAccessor
import sun.reflect.MethodAccessor
import sun.reflect.ConstructorAccessor

object DenyAccessor : MethodAccessor, FieldAccessor, ConstructorAccessor {
    override fun invoke(p0: Any?, p1: Array<out Any>?): Any {
        throw IllegalAccessException("爬爬爬")
    }

    override fun getBoolean(p0: Any?): Boolean {
        throw IllegalAccessException("爬爬爬")
    }

    override fun setFloat(p0: Any?, p1: Float) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun getInt(p0: Any?): Int {
        throw IllegalAccessException("爬爬爬")
    }

    override fun setChar(p0: Any?, p1: Char) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun set(p0: Any?, p1: Any?) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun getLong(p0: Any?): Long {
        throw IllegalAccessException("爬爬爬")
    }

    override fun getFloat(p0: Any?): Float {
        throw IllegalAccessException("爬爬爬")
    }

    override fun setShort(p0: Any?, p1: Short) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun setLong(p0: Any?, p1: Long) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun getChar(p0: Any?): Char {
        throw IllegalAccessException("爬爬爬")
    }

    override fun getShort(p0: Any?): Short {
        throw IllegalAccessException("爬爬爬")
    }

    override fun setInt(p0: Any?, p1: Int) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun getByte(p0: Any?): Byte {
        throw IllegalAccessException("爬爬爬")
    }

    override fun get(p0: Any?): Any {
        throw IllegalAccessException("爬爬爬")
    }

    override fun setDouble(p0: Any?, p1: Double) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun getDouble(p0: Any?): Double {
        throw IllegalAccessException("爬爬爬")
    }

    override fun setBoolean(p0: Any?, p1: Boolean) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun setByte(p0: Any?, p1: Byte) {
        throw IllegalAccessException("爬爬爬")
    }

    override fun newInstance(p0: Array<out Any>?): Any {
        throw IllegalAccessException("爬爬爬")
    }
}