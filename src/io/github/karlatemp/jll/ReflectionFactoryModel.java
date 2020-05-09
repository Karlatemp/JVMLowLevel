package io.github.karlatemp.jll;

@SuppressWarnings({"rawtypes"})
public class ReflectionFactoryModel {
    public native java.lang.reflect.Constructor newConstructor(java.lang.Class var0, java.lang.Class[] var1, java.lang.Class[] var2, int var3, int var4, java.lang.String var5, byte[] var6, byte[] var7);

    public native java.lang.reflect.Field newField(java.lang.Class var0, java.lang.String var1, java.lang.Class var2, int var3, int var4, java.lang.String var5, byte[] var6);

    public native java.lang.reflect.Method newMethod(java.lang.Class var0, java.lang.String var1, java.lang.Class[] var2, java.lang.Class var3, java.lang.Class[] var4, int var5, int var6, java.lang.String var7, byte[] var8, byte[] var9, byte[] var10);

    private static native void checkInitted();

    public native java.lang.reflect.Constructor copyConstructor(java.lang.reflect.Constructor var0);

    public native java.lang.reflect.Field copyField(java.lang.reflect.Field var0);

    public native java.lang.reflect.Method copyMethod(java.lang.reflect.Method var0);

    public native byte[] getExecutableTypeAnnotationBytes(java.lang.reflect.Executable var0);

    public static native sun.reflect.ReflectionFactory getReflectionFactory();

    public native sun.reflect.FieldAccessor newFieldAccessor(java.lang.reflect.Field var0, boolean var1);

    public native sun.reflect.MethodAccessor getMethodAccessor(java.lang.reflect.Method var0);

    public native sun.reflect.MethodAccessor newMethodAccessor(java.lang.reflect.Method var0);

    public native void setMethodAccessor(java.lang.reflect.Method var0, sun.reflect.MethodAccessor var1);

    public native sun.reflect.ConstructorAccessor getConstructorAccessor(java.lang.reflect.Constructor var0);

    public native sun.reflect.ConstructorAccessor newConstructorAccessor(java.lang.reflect.Constructor var0);

    public native void setConstructorAccessor(java.lang.reflect.Constructor var0, sun.reflect.ConstructorAccessor var1);

    static native boolean access$002(boolean var0);

    static native int access$102(int var0);

    static native boolean access$202(boolean var0);

    private final native java.lang.invoke.MethodHandle findReadWriteObjectForSerialization(java.lang.Class var0, java.lang.String var1, java.lang.Class var2);

    private final native java.lang.reflect.Constructor generateConstructor(java.lang.Class var0, java.lang.reflect.Constructor var1);

    private native java.lang.invoke.MethodHandle getReplaceResolveForSerialization(java.lang.Class var0, java.lang.String var1);

    public final native boolean hasStaticInitializerForSerialization(java.lang.Class var0);

    static native int inflationThreshold();

    private static native sun.reflect.LangReflectAccess langReflectAccess();

    public final native java.lang.reflect.Constructor newConstructorForExternalization(java.lang.Class var0);

    public final native java.lang.reflect.Constructor newConstructorForSerialization(java.lang.Class var0);

    public native java.lang.reflect.Constructor newConstructorForSerialization(java.lang.Class var0, java.lang.reflect.Constructor var1);

    public final native java.io.OptionalDataException newOptionalDataExceptionForSerialization(boolean var0);

    private static native boolean packageEquals(java.lang.Class var0, java.lang.Class var1);

    public final native java.lang.invoke.MethodHandle readObjectForSerialization(java.lang.Class var0);

    public final native java.lang.invoke.MethodHandle readObjectNoDataForSerialization(java.lang.Class var0);

    public final native java.lang.invoke.MethodHandle readResolveForSerialization(java.lang.Class var0);

    public native void setLangReflectAccess(sun.reflect.LangReflectAccess var0);

    public final native java.lang.invoke.MethodHandle writeObjectForSerialization(java.lang.Class var0);

    public final native java.lang.invoke.MethodHandle writeReplaceForSerialization(java.lang.Class var0);
}