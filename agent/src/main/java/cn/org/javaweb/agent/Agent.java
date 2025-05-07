package cn.org.javaweb.agent;

import java.lang.instrument.Instrumentation;

//MANIFEST.MF需要配置Premain-Class: cn.org.javaweb.agent.Agent
//输出执行命令的Premain，对应文章的case3
public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new AgentTransform());
    }
}