package cn.org.javaweb.agent.premain;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;



//MANIFEST.MF需要配置Premain-Class: cn.org.javaweb.agent.premain.premainAgent
//表达式注入RASP，对应文章目录4.1
public class premainAgent implements Opcodes {
    private static List<MethodHookDesc> expClassList = new ArrayList<MethodHookDesc>();

    static {
        expClassList.add(new MethodHookDesc("org.mvel2.MVEL", "eval",
                "(Ljava/lang/String;)Ljava/lang/Object;"));
        expClassList.add(new MethodHookDesc("ognl.Ognl", "parseExpression",
                "(Ljava/lang/String;)Ljava/lang/Object;"));
        expClassList.add(new MethodHookDesc("org.springframework.expression.spel.standard.SpelExpression", "<init>",
                "(Ljava/lang/String;Lorg/springframework/expression/spel/ast/SpelNodeImpl;" +
                        "Lorg/springframework/expression/spel/SpelParserConfiguration;)V"));
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("agentArgs : " + agentArgs);
        instrumentation.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                final String class_name = className.replace("/", ".");

                for (final MethodHookDesc methodHookDesc : expClassList) {
                    if (methodHookDesc.getHookClassName().equals(class_name)) {
                        final ClassReader classReader = new ClassReader(classfileBuffer);
                        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                        final int api = ASM5;

                        try {
                            ClassVisitor classVisitor = new ClassVisitor(api, classWriter) {
                                @Override
                                public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
                                    final MethodVisitor methodVisitor = super.visitMethod(i, s, s1, s2, strings);

                                    if (methodHookDesc.getHookMethodName().equals(s) && methodHookDesc.getHookMethodArgTypeDesc().equals(s1)) {
                                        return new MethodVisitor(api, methodVisitor) {
                                            @Override
                                            public void visitCode() {
                                                if ("ognl.Ognl".equals(class_name)||"org.mvel2.MVEL".equals(class_name)) {
                                                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                                                }else {
                                                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1);
                                                }
                                                methodVisitor.visitMethodInsn(
                                                        Opcodes.INVOKESTATIC, premainAgent.class.getName().replace(".", "/"), "expression", "(Ljava/lang/String;)V", false
                                                );
                                            }
                                        };
                                    }
                                    return methodVisitor;
                                }
                            };
                            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                            classfileBuffer = classWriter.toByteArray();
                        }catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
                return classfileBuffer;
            }
        });
    }

    public static void expression(String exp_demo) {
        System.err.println("---------------------------------EXP-----------------------------------------");
        System.err.println(exp_demo);
        System.err.println("---------------------------------调用链---------------------------------------");

        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : elements) {
            System.err.println(element);
        }

        System.err.println("-----------------------------------------------------------------------------");
    }
}
