package com.example.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2018/9/29
 * Title: 用来标识该Activity使用了BindView
 * Description:
 *
 * @author Android-张康
 * update 2018/9/29
 */
@Retention(RetentionPolicy.CLASS)
//用来定义可以修饰哪些元素 ElementType.FIELD 能修饰类、接口或枚举类型
@Target(ElementType.TYPE)
public @interface BindViewActivity {
}
