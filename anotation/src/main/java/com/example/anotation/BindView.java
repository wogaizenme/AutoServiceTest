package com.example.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2018/9/29
 * Title: 用来帮助完成findViewBuyId
 * Description:
 *
 * @author Android-张康
 * update 2018/9/29
 */
//用来定义注解的有效范围 RetentionPolicy.CLASS 可以存在于class文件中，运行时无法获取
@Retention(RetentionPolicy.CLASS)
//用来定义可以修饰哪些元素 ElementType.FIELD 能修饰成员变量
@Target(ElementType.FIELD)
public @interface BindView {

    int value() default 0;
}
