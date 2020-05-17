/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/17 13:48:43
 *
 * JVMLowLevel/jdk13/ReflectionModel.java
 */

package io.github.karlatemp.jll.j13;

@SuppressWarnings({"rawtypes"})
public class ReflectionModel {
    public native java.lang.reflect.Constructor newConstructor(java.lang.Class var0, java.lang.Class[] var1, java.lang.Class[] var2, int var3, int var4, java.lang.String var5, byte[] var6, byte[] var7);

    public native java.lang.reflect.Field newField(java.lang.Class var0, java.lang.String var1, java.lang.Class var2, int var3, int var4, java.lang.String var5, byte[] var6);

    public native java.lang.reflect.Method newMethod(java.lang.Class var0, java.lang.String var1, java.lang.Class[] var2, java.lang.Class var3, java.lang.Class[] var4, int var5, int var6, java.lang.String var7, byte[] var8, byte[] var9, byte[] var10);

    public static native jdk.internal.reflect.ReflectionFactory getReflectionFactory();

    public native java.lang.reflect.Constructor copyConstructor(java.lang.reflect.Constructor var0);

    public native java.lang.Object newInstance(java.lang.reflect.Constructor var0, java.lang.Object[] var1, java.lang.Class var2);

    public native java.lang.Class[] getExecutableSharedParameterTypes(java.lang.reflect.Executable var0);

    public native java.lang.reflect.Method copyMethod(java.lang.reflect.Method var0);

    public native java.lang.reflect.Field copyField(java.lang.reflect.Field var0);

    public native byte[] getExecutableTypeAnnotationBytes(java.lang.reflect.Executable var0);

    public native jdk.internal.reflect.FieldAccessor newFieldAccessor(java.lang.reflect.Field var0, boolean var1);

    public native jdk.internal.reflect.MethodAccessor getMethodAccessor(java.lang.reflect.Method var0);

    public native jdk.internal.reflect.MethodAccessor newMethodAccessor(java.lang.reflect.Method var0);

    public native void setMethodAccessor(java.lang.reflect.Method var0, jdk.internal.reflect.MethodAccessor var1);

    public native jdk.internal.reflect.ConstructorAccessor getConstructorAccessor(java.lang.reflect.Constructor var0);

    public native jdk.internal.reflect.ConstructorAccessor newConstructorAccessor(java.lang.reflect.Constructor var0);

    public native void setConstructorAccessor(java.lang.reflect.Constructor var0, jdk.internal.reflect.ConstructorAccessor var1);

    private static native jdk.internal.reflect.LangReflectAccess langReflectAccess();

    private static native void checkInitted();

    private static native java.lang.reflect.Method findMethodForReflection(java.lang.reflect.Method var0);

    private final native java.lang.reflect.Constructor generateConstructor(java.lang.Class var0, java.lang.reflect.Constructor var1);

    public native java.lang.reflect.Method leafCopyMethod(java.lang.reflect.Method var0);

    private static native boolean packageEquals(java.lang.Class var0, java.lang.Class var1);

    private native boolean superHasAccessibleConstructor(java.lang.Class var0);

    private final native java.lang.invoke.MethodHandle findReadWriteObjectForSerialization(java.lang.Class var0, java.lang.String var1, java.lang.Class var2);

    private native java.lang.invoke.MethodHandle getReplaceResolveForSerialization(java.lang.Class var0, java.lang.String var1);

    static native int inflationThreshold();

    public native void setLangReflectAccess(jdk.internal.reflect.LangReflectAccess var0);

    public final native java.lang.reflect.Constructor newConstructorForExternalization(java.lang.Class var0);

    public final native java.lang.reflect.Constructor newConstructorForSerialization(java.lang.Class var0);

    public final native java.lang.reflect.Constructor newConstructorForSerialization(java.lang.Class var0, java.lang.reflect.Constructor var1);

    public final native java.lang.invoke.MethodHandle readObjectForSerialization(java.lang.Class var0);

    public final native java.lang.invoke.MethodHandle readObjectNoDataForSerialization(java.lang.Class var0);

    public final native java.lang.invoke.MethodHandle writeObjectForSerialization(java.lang.Class var0);

    public final native java.lang.invoke.MethodHandle writeReplaceForSerialization(java.lang.Class var0);

    public final native java.lang.invoke.MethodHandle readResolveForSerialization(java.lang.Class var0);

    public final native boolean hasStaticInitializerForSerialization(java.lang.Class var0);

    public final native java.lang.reflect.Constructor newOptionalDataExceptionForSerialization();

}
