package cn.org.javaweb.agent;

import java.lang.instrument.Instrumentation;

//MANIFEST.MF需要配置Premain-Class: cn.org.javaweb.agent.Agent
public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new AgentTransform());
    }
}