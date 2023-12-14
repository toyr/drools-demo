package com.example.droolsdemo.parse;

import javassist.*;

import java.io.IOException;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/6/2 2:27 下午
 */
public class CtClassUtils {


    /**
     * 创建一个class，如果存在则只添加方法
     *
     * @param beanName
     * @param methodCode
     */
    public static CtClass createBeanOrUpdateMethod(String beanName, String methodCode) {
        //获取默认ClassPath 下的 ClassPool
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass;
        try {
            ctClass = pool.get(beanName);
        } catch (NotFoundException e) {
            // 创建一个新类
            ctClass = pool.makeClass(beanName);
        }
        try {
            CtMethod ctMethod = CtNewMethod.make(methodCode, ctClass);
            try {
                // 如果要创建的方法已经存在，则先删除
                CtMethod declaredMethod = ctClass.getDeclaredMethod(ctMethod.getName());
                ctClass.removeMethod(declaredMethod);
            } catch (NotFoundException ignored) {

            }
            ctClass.addMethod(ctMethod);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return ctClass;
    }

    /**
     * 生成字节码文件
     *
     * @param ctClass
     */
    public static void writeFile(CtClass ctClass) {
        try {
            if (ctClass != null) {
                ctClass.writeFile(System.getProperty("user.dir") + "/target/classes");
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
