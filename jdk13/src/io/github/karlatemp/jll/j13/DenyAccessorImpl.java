package io.github.karlatemp.jll.j13;

import jdk.internal.reflect.ConstructorAccessor;
import jdk.internal.reflect.FieldAccessor;
import jdk.internal.reflect.MethodAccessor;

public class DenyAccessorImpl extends DenyAccessor implements
        MethodAccessor, FieldAccessor, ConstructorAccessor {
    public static final DenyAccessorImpl instance = new DenyAccessorImpl();
}
