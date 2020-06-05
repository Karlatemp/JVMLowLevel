/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/06/05 18:38:57
 *
 * JVMLowLevel/JVM-LowLevel.accessor.main/StackFrameWalker.java
 */

package io.github.karlatemp.jll.accessor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class StackFrameWalker extends SecurityManager {
    private static final StackFrameWalker WALKER = new StackFrameWalker();
    private static final Class<Field> FIELD_CLASS = Field.class;
    private static final Class<Method> METHOD_CLASS = Method.class;
    @SuppressWarnings("rawtypes")
    private static final Class<Constructor> CONSTRUCTOR_CLASS = Constructor.class;
    private static final Class<StackFrameWalker> WALKER_CLASS = StackFrameWalker.class;
    private static final Class<SecurityManager> SECURITY_MANAGER_CLASS = SecurityManager.class;

    @SuppressWarnings({"PointlessBooleanExpression", "SpellCheckingInspection"})
    private static boolean isSkippable(Class<?> klass) {
        return false ||
                klass == FIELD_CLASS || klass == METHOD_CLASS ||
                klass == CONSTRUCTOR_CLASS || klass == WALKER_CLASS ||
                klass == SECURITY_MANAGER_CLASS;
    }

    @Nullable
    public static Class<?> findCallerInReflection() {
        final Class<?>[] context = WALKER.getClassContext();
        for (Class<?> frame : context) {
            if (isSkippable(frame)) {
                continue;
            }
            return frame;
        }
        return null;
    }

    @NotNull
    public static Class<?> findCallerInReflectionNotNull() {
        Class<?> caller = findCallerInReflection();
        if (caller == null) throw new NoSuchElementException("No Caller in Current Thread");
        return caller;
    }

    @Nullable
    public static Class<?> findCaller() {
        final Class<?>[] context = WALKER.getClassContext();
        int end = context.length - 1;
        for (int i = 0; i < end; i++) {
            Class<?> frame = context[i];
            if (!isSkippable(frame)) return context[i + 1];
        }
        return null;
    }

    @NotNull
    public static Class<?> findCallerNotNull() {
        Class<?> caller = findCaller();
        if (caller == null) throw new NoSuchElementException("No Caller in Current Thread");
        return caller;
    }

}
