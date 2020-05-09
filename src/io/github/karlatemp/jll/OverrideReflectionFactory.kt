package io.github.karlatemp.jll

import sun.misc.Unsafe
import sun.reflect.ConstructorAccessor
import sun.reflect.FieldAccessor
import sun.reflect.MethodAccessor
import sun.reflect.ReflectionFactory
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

// Don't use object, it is not support
class OverrideReflectionFactory private constructor() : ReflectionFactoryModel() {
    companion object {
        private val unsafeAccess = listOf(
            MethodHandles::class.java,
            MethodHandles.Lookup::class.java,
            Unsafe::class.java,
            ReflectionFactory::class.java,
            MethodHandle::class.java
        )
    }

    private fun isUnsafe(klass: Class<*>): Boolean {
        unsafeAccess.forEach {
            if (it.isAssignableFrom(klass)) return@isUnsafe true
        }
        return false
    }

    override fun newMethodAccessor(var0: Method): MethodAccessor {
        if (isUnsafe(var0.declaringClass) || isUnsafe(var0.returnType))
            return DenyAccessor
        if (var0.declaringClass?.`package`?.name == "io.github.karlatemp.jll") {
            return DenyAccessor
        }
        return super.newMethodAccessor(var0)
    }

    override fun newConstructorAccessor(var0: Constructor<*>): ConstructorAccessor {
        if (isUnsafe(var0.declaringClass))
            return DenyAccessor
        if (var0.declaringClass == MethodHandles.Lookup::class.java) {
            return DenyAccessor
        }
        return super.newConstructorAccessor(var0)
    }

    override fun newFieldAccessor(var0: Field, var1: Boolean): FieldAccessor {
        if (isUnsafe(var0.declaringClass) || isUnsafe(var0.type))
            return DenyAccessor
        return super.newFieldAccessor(var0, var1)
    }
}