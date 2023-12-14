package com.example.droolsdemo.parse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/6/2 3:35 下午
 */
public class RuleUtils {


    /**
     * 匹配指定bean中的指定规则
     *
     * @param beanName
     * @param ruleName
     * @param params
     */
    public static void matchWithRule(String beanName, String ruleName, Map<String, Object> params) {
        try {
            Class<?> ruleClass = Class.forName(beanName);
            Object o = ruleClass.newInstance();
            Method declaredMethod = ruleClass.getDeclaredMethod(ruleName, Map.class);
            Object invoke = declaredMethod.invoke(o, params);
            System.out.println(ruleName + ":" + invoke);
        } catch (ClassNotFoundException e) {
            System.err.println("没有找到对应场景:" + beanName);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.err.println("没有找到对应规则:" + ruleName);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    /**
     * 匹配指定bean中的所有规则
     *
     * @param beanName
     * @param params
     */
    public static void matchAllRules(String beanName, Map<String, Object> params) {
        try {
            Class<?> aClass = Class.forName(beanName);
            Object o = aClass.newInstance();
            Method[] methods = o.getClass().getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass().getName().equals(aClass.getName())) {
                    Object invoke = method.invoke(o, params);
                    System.out.println(method.getName() + ": " + invoke);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("没有找到对应场景:" + beanName);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
