package net.azisaba.theFastLife;

import javassist.ClassPool;
import javassist.CtClass;

import java.lang.instrument.Instrumentation;

public class TheFastLife {
    public static boolean init = false;

    public static void agentmain(String arg, Instrumentation instrumentation) {
        init(instrumentation);
    }

    public static void premain(String arg, Instrumentation instrumentation) {
        init(instrumentation);
    }

    public static void init(Instrumentation inst) {
        if (init) return;
        inst.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            if (className.equals("net/minecraft/server/MinecraftServer")) {
                try {
                    ClassPool cp = ClassPool.getDefault();
                    CtClass cc = cp.get("net.minecraft.server.MinecraftServer");
                    cc.getMethod("canSleepForTickNoOversleep", "()Z").setBody("{return false;}");
                    System.out.println("Redefined class " + className);
                    return cc.toBytecode();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });
        init = true;
    }
}
