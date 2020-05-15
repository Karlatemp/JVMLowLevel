package io.github.karlatemp.jll

import org.objectweb.asm.*

open class BytecodeScanner {
    open fun visitClass(klass: String) {}
    open fun visitField(klass: String, field: String, desc: String) {}
    open fun visitMethod(klass: String, name: String, desc: String, opcode: Int) {}
    fun visitDescription(descriptor: String) {
        Type.getReturnType(descriptor).let {
            var i = it
            while (i.sort == Type.ARRAY) i = i.elementType
            i
        }.takeIf { it.sort == Type.OBJECT }?.apply {
            visitClass(this.internalName)
        }
    }

    fun view(visitor: ClassVisitor.() -> Unit) {
        ClassView().visitor()
    }

    private inner class ClassView : ClassVisitor(Opcodes.ASM7) {
        private lateinit var name: String

        override fun visit(
            version: Int,
            access: Int,
            name: String,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            visitClass(name)
            this.name = name
            superName?.apply { visitClass(this) }
            interfaces?.apply { forEach { visitClass(it) } }
        }

        override fun visitField(
            access: Int,
            name: String,
            descriptor: String,
            signature: String?,
            value: Any?
        ): FieldVisitor? {
            visitField(this.name, name, descriptor)
            return null
        }

        override fun visitMethod(
            access: Int,
            name: String,
            descriptor: String,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            visitDescription(descriptor)
            return object : MethodVisitor(Opcodes.ASM7) {
                override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
                    visitField(owner, name, descriptor)
                }

                override fun visitMethodInsn(
                    opcode: Int,
                    owner: String,
                    name: String,
                    descriptor: String,
                    isInterface: Boolean
                ) {
                    visitClass(owner)
                    visitDescription(descriptor)
                    visitMethod(owner, name, descriptor, opcode)
                }

                override fun visitTypeInsn(opcode: Int, type: String) {
                    visitClass(type)
                }

                override fun visitInvokeDynamicInsn(
                    name: String,
                    descriptor: String,
                    bootstrapMethodHandle: Handle,
                    vararg bootstrapMethodArguments: Any?
                ) {
                    bootstrapMethodHandle.let {
                        visitDescription(it.desc)
                        visitMethod(it.owner, it.name, it.desc, it.tag)
                    }
                }
            }
        }
    }
}