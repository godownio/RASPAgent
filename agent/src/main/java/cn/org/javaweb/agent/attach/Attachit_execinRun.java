package cn.org.javaweb.agent.attach;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;


//JDK>=9
public class Attachit_execinRun {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            if (vmd.displayName().endsWith("MainTest")) {
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("E:\\CODE_COLLECT\\Idea_java_ProTest\\javawebAgent\\agent\\target\\agent-1.0.0-shaded.jar", "Attach!");
                System.out.println("ok");
                virtualMachine.detach();
            }
        }
    }
}