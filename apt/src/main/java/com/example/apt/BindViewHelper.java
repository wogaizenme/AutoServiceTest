package com.example.apt;

import java.lang.reflect.Constructor;

/**
 * Created on 2018/9/30
 * Title: 帮助完成绑定View操作
 * Description: 在Activity的setContentView方法之后调用，自动生成一个协助类，帮助完成Activity的初始化
 * 工作
 *
 * @author Android-张康
 * update 2018/9/30
 */
public class BindViewHelper {

    public static void bind(Object target) {
        String classFullName = target.getClass().getName() + "$$BindView";
        try {
            Class proxy = Class.forName(classFullName);
            Constructor constructor = proxy.getConstructor(target.getClass());
            constructor.newInstance(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
