package com.example.apt;

import java.lang.reflect.Constructor;

/**
 * Created on 2018/9/30
 * Title:
 * Description:
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
