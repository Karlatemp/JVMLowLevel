/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:09:43
 *
 * JVMLowLevel/JVM-LowLevel.accessor.main/FieldAccessorMarker.kt
 */

package io.github.karlatemp.jll.accessor

interface FieldAccessorMarker {
    @Throws(IllegalArgumentException::class)
    fun get(thiz: Any?): Any?

    @Throws(IllegalArgumentException::class)
    fun getBoolean(thiz: Any?): Boolean

    @Throws(IllegalArgumentException::class)
    fun getByte(thiz: Any?): Byte

    @Throws(IllegalArgumentException::class)
    fun getChar(thiz: Any?): Char

    @Throws(IllegalArgumentException::class)
    fun getShort(thiz: Any?): Short

    @Throws(IllegalArgumentException::class)
    fun getInt(thiz: Any?): Int

    @Throws(IllegalArgumentException::class)
    fun getLong(thiz: Any?): Long

    @Throws(IllegalArgumentException::class)
    fun getFloat(thiz: Any?): Float

    @Throws(IllegalArgumentException::class)
    fun getDouble(thiz: Any?): Double

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun set(thiz: Any?, value: Any?)

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun setBoolean(thiz: Any?, value: Boolean)

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun setByte(thiz: Any?, value: Byte)

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun setChar(thiz: Any?, value: Char)

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun setShort(thiz: Any?, value: Short)

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun setInt(thiz: Any?, value: Int)

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun setLong(thiz: Any?, value: Long)

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun setFloat(thiz: Any?, value: Float)

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun setDouble(thiz: Any?, value: Double)

}