package com.example.apt;

import com.example.anotation.BindViewActivity;
import com.example.anotation.BindView;
import com.example.anotation.ViewClick;
import com.example.anotation.ViewClicks;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created on 2018/9/29
 * Title:
 * Description:
 *
 * @author Android-张康
 * update 2018/9/29
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Elements mElementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindViewActivity.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //获取目标注解
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(BindViewActivity.class);
        if (null == elementsAnnotatedWith) {
            return false;
        }
        for (Element element : elementsAnnotatedWith) {
            //获取当前使用注解LActivity注解的类
            TypeElement typeElement = (TypeElement) element;
            //创建一个成员变量,用来保存当前的Activity
            FieldSpec fieldSpec = FieldSpec
                    .builder(ClassName.get(typeElement.asType()), "mActivity", Modifier.PRIVATE)
                    .build();
            //创建一个构造函数
            MethodSpec.Builder methodSpecConstructorBuilder =
                    //创建一个构造函数
                    MethodSpec.constructorBuilder()
                            //设置修饰符
                            .addModifiers(Modifier.PUBLIC)
                            //设置参数
                            .addParameter(ClassName.get(typeElement.asType()), "activity")
                            .addStatement("this.mActivity = activity");
            //生成一个静态方法
            MethodSpec.Builder findViewById = null;
            MethodSpec.Builder viewClicks = null;
            MethodSpec.Builder viewClick = null;
            //获取到当前类中所有的注解
            List<? extends Element> allMembers = mElementUtils.getAllMembers(typeElement);
            for (Element item : allMembers) {
                //获取LView注解
                BindView bindView = item.getAnnotation(BindView.class);
                if (null != bindView) {
                    if (null == findViewById) {
                        findViewById = getFindViewByIdMethod();
                    }
                    //如果是当前注解是LView,生成一行findViewById
                    findViewById.addStatement(
                            String.format("mActivity.%1$s = (%2$s) mActivity.findViewById(%3$s)",
                                    item.getSimpleName(), ClassName.get(item.asType()), bindView.value())
                    );
                    continue;
                }
                ViewClicks lViewClicks = item.getAnnotation(ViewClicks.class);
                if (null != lViewClicks) {
                    //检查需要的参数是否合法，支持无参和一个View参数的方法
                    ExecutableElement executableElement = (ExecutableElement) item;
                    List<? extends VariableElement> parameters = executableElement.getParameters();
                    if (null != parameters && parameters.size() > 1) {
                        throw new RuntimeException("暂时只支持无参或者一个参数");
                    } else if (null != parameters && parameters.size() == 1) {
                        if(!"android.view.View".equals(parameters.get(0).asType().toString())){
                            throw new RuntimeException("请检查入参是不是 android.view.View ");
                        }
                    }
                    if (null == viewClicks) {
                        viewClicks = getViewClicksMethod();
                    }
                    String format = null == parameters || parameters.size() == 0 ? item.getSimpleName() + "()" : item.getSimpleName() + "(v)";
                    viewClicks.addStatement(String.format("android.view.View.OnClickListener onClickListener = new android.view.View.OnClickListener() {\n" +
                            "            @Override\n" +
                            "            public void onClick(android.view.View v) {\n" +
                            "                mActivity.%1$s;\n" +
                            "            }\n" +
                            "        };", format));
                    int[] value = lViewClicks.value();
                    for (int id : value) {
                        viewClicks.addStatement(
                                String.format("mActivity.findViewById(%1$s).setOnClickListener(onClickListener)", id));
                    }
                    continue;
                }
                ViewClick lViewClick = item.getAnnotation(ViewClick.class);
                if (null != lViewClick) {
                    //检查需要的参数是否合法，支持无参和一个View参数的方法
                    ExecutableElement executableElement = (ExecutableElement) item;
                    List<? extends VariableElement> parameters = executableElement.getParameters();
                    if (null != parameters && parameters.size() > 1) {
                        throw new RuntimeException("暂时只支持无参或者一个参数");
                    } else if (null != parameters && parameters.size() == 1) {
                        if(!"android.view.View".equals(parameters.get(0).asType().toString())){
                            throw new RuntimeException("请检查入参是不是 android.view.View ");
                        }
                    }
                    if (null == viewClick) {
                        viewClick = getViewClickMethod();
                    }
                    String format = null == parameters || parameters.size() == 0 ? item.getSimpleName() + "()" : item.getSimpleName() + "(v)";
                    viewClick.addStatement(String.format(" mActivity.findViewById(%1$s).setOnClickListener(new android.view.View.OnClickListener() {\n" +
                            "            @Override\n" +
                            "            public void onClick(android.view.View v) {\n" +
                            "                mActivity.%2$s;\n" +
                            "            }\n" +
                            "        });", lViewClick.value(), format));
                }
            }
            TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(typeElement.getSimpleName()+"$$BindView")
                    .addField(fieldSpec)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
            //如果findViewById方法存在，添加
            if (null != findViewById) {
                methodSpecConstructorBuilder.addStatement("findViewById()");
                typeSpecBuilder.addMethod(findViewById.build());
            }
            //如果viewClicks方法存在，添加
            if (null != viewClicks) {
                methodSpecConstructorBuilder.addStatement("viewClicks()");
                typeSpecBuilder.addMethod(viewClicks.build());
            }
            //如果viewClick方法存在，添加
            if (null != viewClick) {
                methodSpecConstructorBuilder.addStatement("viewClick()");
                typeSpecBuilder.addMethod(viewClick.build());
            }
            //添加构造方法
            typeSpecBuilder.addMethod(methodSpecConstructorBuilder.build());
            JavaFile javaFile = JavaFile.builder(ProcessorTools.getPackageName(mElementUtils, typeElement), typeSpecBuilder.build())
                    .build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private MethodSpec.Builder getFindViewByIdMethod() {
        return MethodSpec.methodBuilder("findViewById")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.VOID);
    }

    private MethodSpec.Builder getViewClicksMethod() {
        return MethodSpec.methodBuilder("viewClicks")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.VOID);
    }

    private MethodSpec.Builder getViewClickMethod() {
        return MethodSpec.methodBuilder("viewClick")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.VOID);
    }
}
