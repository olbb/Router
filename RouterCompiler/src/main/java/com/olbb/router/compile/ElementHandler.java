package com.olbb.router.compile;

import com.olbb.router.RouteMeta;
import com.olbb.router.RouteType;
import com.olbb.router.annotations.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.olbb.router.Constant.ACTIVITY;
import static com.olbb.router.Constant.FRAGMENT;
import static com.olbb.router.Constant.FRAGMENT_V4;
import static com.olbb.router.Constant.METHOD_LOAD_INTO;
import static com.olbb.router.Constant.WARNING_TIPS;
import static javax.lang.model.element.Modifier.PUBLIC;

public class ElementHandler {

    Filer filer;
    Elements elements;
    Types types;

    public ElementHandler(Filer filer, Elements elementUtils, Types typeUtils) {
        this.filer = filer;
        this.elements = elementUtils;
        types = typeUtils;
    }

    /**
     * 表示一个程序元素，比如包、类或者方法，有如下几种子接口：
     * ExecutableElement：表示某个类或接口的方法、构造方法或初始化程序（静态或实例），包括注解类型元素 ；
     * PackageElement：表示一个包程序元素；
     * TypeElement：表示一个类或接口程序元素；
     * TypeParameterElement：表示一般类、接口、方法或构造方法元素的形式类型参数；
     * VariableElement：表示一个字段、enum 常量、方法或构造方法参数、局部变量或异常参数
     */
    public void handleElement(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        TypeMirror type_Activity = elements.getTypeElement(ACTIVITY).asType();
        TypeMirror fragmentTm = elements.getTypeElement(FRAGMENT).asType();
        TypeMirror fragmentTmV4 = elements.getTypeElement(FRAGMENT_V4).asType();
        PackageElement packageElement = null;
        List<RouteMeta> routeMetas = new ArrayList<>();
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Router.class)) {
            ElementKind kind = element.getKind();
//            System.out.println("kind is :" + kind);
//            System.out.println("element is :" + element);

            if (kind == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                TypeMirror tm = typeElement.asType();
                Router router = typeElement.getAnnotation(Router.class);
                System.out.println("annotation is:" + router);
                if (packageElement == null) {
                    packageElement = elements.getPackageOf(typeElement);
                }
                RouteMeta routeMeta = null;
                if (types.isSubtype(tm, type_Activity)) {                 // Activity
                    routeMeta = new RouteMeta(router, element, RouteType.ACTIVITY, null);
                } else if (types.isSubtype(tm, fragmentTm) || types.isSubtype(tm, fragmentTmV4)) {//Fragment
                    routeMeta = new RouteMeta(router, element, RouteType.FRAGMENT, null);
                } else {
                    System.out.println("unSupport type:" + element);
                    continue;
                }
                routeMetas.add(routeMeta);
            }
        }

        try {
            // Generate groups
            if (packageElement == null) return;
            String groupFileName = "EBookRoute";
            TypeElement type_IRouteGroup = elements.getTypeElement("com.olbb.router.IRouteGroup");
            ParameterSpec groupParamSpec = ParameterSpec.builder(inputMapTypeOfGroup, "atlas").build();
            MethodSpec.Builder loadIntoMethodOfGroupBuilder = MethodSpec.methodBuilder(METHOD_LOAD_INTO)
                    .addAnnotation(Override.class)
                    .addModifiers(PUBLIC)
                    .addParameter(groupParamSpec);
            ClassName routeMetaCn = ClassName.get(RouteMeta.class);
            ClassName routeTypeCn = ClassName.get(RouteType.class);

            for (RouteMeta routeMeta : routeMetas) {
                loadIntoMethodOfGroupBuilder.addStatement(
                        "atlas.put($S, $T.build($T." + routeMeta.getType() + ", $T.class, $S, $S, " + null + ", " + routeMeta.getPriority() + ", " + routeMeta.getExtra() + "))",
                        routeMeta.getPath(),
                        routeMetaCn,
                        routeTypeCn,
                        ClassName.get((TypeElement) routeMeta.getRawType()),
                        routeMeta.getPath().toLowerCase(),
                        routeMeta.getGroup().toLowerCase());
            }

            JavaFile.builder(packageElement.getQualifiedName().toString(),
                    TypeSpec.classBuilder(groupFileName)
                            .addJavadoc(WARNING_TIPS)
                            .addSuperinterface(ClassName.get(type_IRouteGroup))
                            .addModifiers(PUBLIC)
                            .addMethod(loadIntoMethodOfGroupBuilder.build())
                            .build()
            ).build().writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(
            ClassName.get(Map.class),
            ClassName.get(String.class),
            ClassName.get(RouteMeta.class)
    );

    private void genSpec(List<RouteMeta> routeMetas) {

    }
}
