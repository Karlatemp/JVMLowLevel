/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/17 13:48:43
 *
 * JVMLowLevel/jdk13/DenyAccessorImpl.java
 */

package io.github.karlatemp.jll.j13;

import jdk.internal.reflect.ConstructorAccessor;
import jdk.internal.reflect.FieldAccessor;
import jdk.internal.reflect.MethodAccessor;

public class DenyAccessorImpl extends DenyAccessor implements
        MethodAccessor, FieldAccessor, ConstructorAccessor {
    public static final DenyAccessorImpl instance = new DenyAccessorImpl();
}
