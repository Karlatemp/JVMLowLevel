package io.github.karlatemp.jll.j13;

import jdk.internal.misc.Unsafe;
import jdk.internal.reflect.Reflection;
import jdk.internal.reflect.ReflectionFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/*
 * 为什么不写在kotlin里面? 因为kotlin似乎不支持 --add-exports
 */
public class A {
    private static final long classReflectionDataOffset;
    private static final Unsafe UNSAFE;

    static {
        AS.INSTANCE.getClass().getClassLoader();
        var unsafe = UNSAFE = Unsafe.getUnsafe();
        unsafe.ensureClassInitialized(ClassWriter.class);
        unsafe.ensureClassInitialized(ClassReader.class);
        unsafe.ensureClassInitialized(ClassRemapper.class);
        unsafe.ensureClassInitialized(SimpleRemapper.class);
        AS.createAccessor();
        Object factory;
        var factory0 = ReflectionFactory.getReflectionFactory();
        try {
            factory = unsafe.allocateInstance(Class.forName("io.github.karlatemp.jll.j13.OverrideReflectionFactory"));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
        System.out.println(factory.getClass().getSuperclass());
        if (!(factory instanceof ReflectionFactory)) {
            throw new ExceptionInInitializerError("New Factory not JDK factory");
        }
        classReflectionDataOffset = unsafe.objectFieldOffset(Class.class, "reflectionData");
        clearReflectionData();
        Field[] fields;
        try {
            var met = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
            fields = (Field[]) factory0.newMethodAccessor(met).invoke(Reflection.class, new Object[]{false});
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
        var reflectionBase = unsafe.staticFieldBase(findField("fieldFilterMap", fields));
        long
                fieldFilterMapOffset = unsafe.staticFieldOffset(findField("fieldFilterMap", fields)),
                methodFilterMapOffset = unsafe.staticFieldOffset(findField("methodFilterMap", fields));
        var fieldFilterMap = unsafe.getReference(reflectionBase, fieldFilterMapOffset);
        var methodFilterMap = unsafe.getReference(reflectionBase, methodFilterMapOffset);
        unsafe.putReference(reflectionBase, fieldFilterMapOffset, new HashMap<>());
        unsafe.putReference(reflectionBase, methodFilterMapOffset, new HashMap<>());

        clearReflectionData();
        for (var klass : AS.instrumentation.getAllLoadedClasses()) {
            for (var field : klass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    if (!field.getType().isPrimitive()) {
                        var base = unsafe.staticFieldBase(field);
                        var offset = unsafe.staticFieldOffset(field);
                        var value = unsafe.getReference(base, offset);
                        if (value instanceof ReflectionFactory) {
                            unsafe.putReference(base, offset, factory);
                        }
                    }
                }
            }
        }

        unsafe.putReference(reflectionBase, fieldFilterMapOffset, fieldFilterMap);
        unsafe.putReference(reflectionBase, methodFilterMapOffset, methodFilterMap);
        clearReflectionData();
    }

    private static Field findField(String name, Field[] fields) {
        for (var f : fields) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        throw new ExceptionInInitializerError("No field " + name + " found.");
    }

    public static void clearReflectionData() {
        for (var klass : AS.instrumentation.getAllLoadedClasses()) {
            UNSAFE.putReferenceVolatile(klass, classReflectionDataOffset, null);
        }
    }

    public static void main(String[] args) throws Throwable {
        var implField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        implField.setAccessible(true);
        System.out.println(implField.get(null));
    }
}
