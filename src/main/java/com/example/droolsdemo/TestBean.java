package com.example.droolsdemo;

import javassist.*;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xiegaobing
 * @description:
 * @date 2023/6/1 2:51 下午
 */
public class TestBean {
    @Test
    public void test1() {

        ClassPool pool = ClassPool.getDefault();

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        pool.insertClassPath(new LoaderClassPath(cl));

        try {
            //获取指定类CtClass
            CtClass ctClass = pool.get("com.example.droolsdemo.TestBean");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        //创建新类
        CtClass newCtClass = pool.makeClass("com.example.droolsdemo.parse.DestBean");
        //将修改后的CtClass加载至当前线程的上下文类加载器中
        try {
            pool.toClass(newCtClass);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        //获取默认ClassPath 下的 ClassPool
        ClassPool pool = ClassPool.getDefault();

        // 创建一个新类
        CtClass ctClass = pool.makeClass("com.example.droolsdemo.parse.CreateBean");

        //添加一个int类型的，名字为value的变量，以及getter/setter
        try {
            CtField ctField = new CtField(CtClass.intType,"value",ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);

            //使用CtNewMethod添加getter/setter
            CtMethod getter = CtNewMethod.getter("getValue", ctField);
            CtMethod setter = CtNewMethod.setter("setValue", ctField);
            ctClass.addMethod(getter);
            ctClass.addMethod(setter);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        //生成字节码文件，方便查看创建出来的类的结果
        try {
            String directoryName = System.getProperty("user.dir") + "/target/classes";
            ctClass.writeFile(directoryName);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test3() {
        //获取默认ClassPath 下的 ClassPool
        ClassPool pool = ClassPool.getDefault();

        // 创建一个新类
        CtClass ctClass = pool.makeClass("com.example.droolsdemo.parse.CreateBean1");

        //添加一个hello的方法
        try {
            CtMethod ctMethod = new CtMethod(CtClass.intType, "hello", new CtClass[]{CtClass.intType, CtClass.doubleType}, ctClass);
            ctMethod.setModifiers(Modifier.PUBLIC);
            ctMethod.setBody("return $1 + $2;");
            //在方法体的前后分别插入代码
            ctMethod.insertBefore("System.out.println(\"在前面插入了：\" + $1);");
            ctMethod.insertAfter("System.out.println(\"在最后插入了：\" + $1);");
            ctClass.addMethod(ctMethod);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }



        //生成字节码文件，方便查看创建出来的类的结果
        try {
            ctClass.writeFile(System.getProperty("user.dir") + "/target/classes");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test4() {
        //获取默认ClassPath 下的 ClassPool
        ClassPool pool = ClassPool.getDefault();
        // 创建一个新类
        CtClass ctClass = pool.makeClass("com.example.droolsdemo.parse.CreateBean");
        try {
            //添加一个int类型的，名字为value的变量，以及getter/setter
            CtField ctField = new CtField(CtClass.intType,"value",ctClass);
            ctField.setModifiers(Modifier.PRIVATE);
            ctClass.addField(ctField);

            //使用CtNewMethod添加getter/setter
            CtMethod getter = CtNewMethod.getter("getValue", ctField);
            CtMethod setter = CtNewMethod.setter("setValue", ctField);
            ctClass.addMethod(getter);
            ctClass.addMethod(setter);

            //添加一个hello2的方法
            CtMethod ctMethod = CtNewMethod.make("public int hello2(int num1, double num2) {\n" +
                    "    double sum = (double)num1 + num2;\n" +
                    "    return (int)sum;\n" +
                    "}", ctClass);
            ctClass.addMethod(ctMethod);

        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

        try {
            Object o = ctClass.toClass().newInstance();
            Method hello2 = o.getClass().getMethod("hello2", int.class, double.class);
            Object invoke = hello2.invoke(o, 1, 2);
            System.out.println(invoke);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        //生成字节码文件，方便查看创建出来的类的结果
        try {
            ctClass.writeFile(System.getProperty("user.dir") + "/target/classes");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
