package com.github.benshi.mybatis.flex.plugin.parse;

import javax.lang.model.element.Modifier;

import com.github.benshi.mybatis.flex.plugin.LogHolder;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * 
 * 
 * @date 2025年6月18日
 * @time 22:48:34
 * @description
 * 
 */
public class JavaFileVisitor extends VoidVisitorAdapter<Void> {
    private final ParsedJavaFile result;

    public JavaFileVisitor(ParsedJavaFile result) {
        this.result = result;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        result.setClassName(n.getNameAsString());
        result.setClassBuilder(TypeSpec.classBuilder(result.getClassName())
                .addModifiers(Modifier.PUBLIC));

        LogHolder.debug("Found class: " + n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(FieldDeclaration n, Void arg) {
        n.getVariables().forEach(variable -> {
            String fieldName = variable.getNameAsString();
            String typeName = n.getElementType().asString();

            FieldSpec.Builder fieldBuilder = FieldSpec.builder(
                    ClassName.bestGuess(typeName),
                    fieldName,
                    Modifier.PRIVATE);

            result.getFields().add(fieldBuilder.build());
            LogHolder.debug("Found field: " + fieldName + " of type " + typeName);
        });

        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        String methodName = n.getNameAsString();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC);

        // 处理返回类型
        if (!n.getType().isVoidType()) {
            String returnType = n.getType().asString();
            methodBuilder.returns(ClassName.bestGuess(returnType));
        }

        // 处理参数
        n.getParameters().forEach(param -> {
            String paramName = param.getNameAsString();
            String paramType = param.getType().asString();
            methodBuilder.addParameter(ClassName.bestGuess(paramType), paramName);
        });

        result.getMethods().add(methodBuilder.build());
        LogHolder.debug("Found method: " + methodName);

        super.visit(n, arg);
    }

}
