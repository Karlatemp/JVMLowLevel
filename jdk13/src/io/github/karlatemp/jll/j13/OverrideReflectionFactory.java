/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/17 13:48:43
 *
 * JVMLowLevel/jdk13/OverrideReflectionFactory.java
 */

package io.github.karlatemp.jll.j13;

import jdk.internal.misc.Unsafe;
import jdk.internal.reflect.ConstructorAccessor;
import jdk.internal.reflect.FieldAccessor;
import jdk.internal.reflect.MethodAccessor;
import jdk.internal.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class OverrideReflectionFactory extends ReflectionModel {
    public static final Set<Class<?>> unsafe = new HashSet<>(Set.of(
            Class.class,
            ReflectionFactory.class,
            Unsafe.class,
            sun.misc.Unsafe.class,
            MethodHandles.class,
            MethodHandles.Lookup.class,
            MethodHandle.class
    ));

    public static boolean isUnsafe(Class<?> klass) {
        for (var c : unsafe) {
            if (c.isAssignableFrom(klass)) return true;
        }
        return false;
    }

    @Override
    public ConstructorAccessor newConstructorAccessor(Constructor var0) {
        if (isUnsafe(var0.getDeclaringClass()))
            return DenyAccessorImpl.instance;
        return super.newConstructorAccessor(var0);
    }

    @Override
    public MethodAccessor newMethodAccessor(Method var0) {
        if (isUnsafe(var0.getReturnType()) || isUnsafe(var0.getDeclaringClass()))
            return DenyAccessorImpl.instance;
        return super.newMethodAccessor(var0);
    }

    @Override
    public FieldAccessor newFieldAccessor(Field var0, boolean var1) {
        if (isUnsafe(var0.getType()) || isUnsafe(var0.getDeclaringClass()))
            return DenyAccessorImpl.instance;
        return super.newFieldAccessor(var0, var1);
    }
}
