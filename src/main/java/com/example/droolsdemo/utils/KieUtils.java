package com.example.droolsdemo.utils;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.spring.KModuleBeanFactoryPostProcessor;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/5/22 1:49 下午
 */
public class KieUtils {

    private static KieContainer kieContainer;

    private static KieSession kieSession;

    private static KModuleBeanFactoryPostProcessor kModuleBeanFactoryPostProcessor;

    public static KieContainer getKieContainer() {
        return kieContainer;
    }

    public static void setKieContainer(KieContainer kieContainer) {
        KieUtils.kieContainer = kieContainer;
        kieSession = kieContainer.newKieSession();
    }

    public static KieSession getKieSession() {
        return kieSession;
    }

    public static void setKieSession(KieSession kieSession) {
        KieUtils.kieSession = kieSession;
    }

    public static KieServices getKieServices() {
        return KieServices.Factory.get();
    }

    public static KModuleBeanFactoryPostProcessor getkModuleBeanFactoryPostProcessor() {
        return kModuleBeanFactoryPostProcessor;
    }

    public static void setkModuleBeanFactoryPostProcessor(KModuleBeanFactoryPostProcessor kModuleBeanFactoryPostProcessor) {
        KieUtils.kModuleBeanFactoryPostProcessor = kModuleBeanFactoryPostProcessor;
    }
}
