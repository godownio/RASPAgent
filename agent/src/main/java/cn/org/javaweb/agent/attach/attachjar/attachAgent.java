package cn.org.javaweb.agent.attach.attachjar;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

//agentmain RASP，对应文章4.2
public class attachAgent {

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
//        for (Class clazz : inst.getAllLoadedClasses()) {
//            System.out.println(clazz.getName());
//        }
        CustomClassTransformer transformer = new CustomClassTransformer(inst);
        transformer.retransform();
    }
}