package com.example.apt;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created on 2018/9/29
 * Title:
 * Description:
 *
 * @author Android-张康
 * update 2018/9/29
 */
public class ProcessorTools {

    public static String getPackageName(Elements elements,TypeElement type) {
        return elements.getPackageOf(type).getQualifiedName().toString();
    }
}
