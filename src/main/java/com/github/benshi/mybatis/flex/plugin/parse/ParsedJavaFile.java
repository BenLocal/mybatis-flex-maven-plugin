package com.github.benshi.mybatis.flex.plugin.parse;

import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * 
 * 
 * @date 2025年6月18日
 * @time 22:44:21
 * @description
 * 
 */
public class ParsedJavaFile {
    private String packageName;
    private String className;
    private List<FieldSpec> fields = new ArrayList<>();
    private List<MethodSpec> methods = new ArrayList<>();
    private List<String> imports = new ArrayList<>();
    private TypeSpec.Builder classBuilder;

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the fields
     */
    public List<FieldSpec> getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(List<FieldSpec> fields) {
        this.fields = fields;
    }

    /**
     * @return the methods
     */
    public List<MethodSpec> getMethods() {
        return methods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(List<MethodSpec> methods) {
        this.methods = methods;
    }

    /**
     * @return the imports
     */
    public List<String> getImports() {
        return imports;
    }

    /**
     * @param imports the imports to set
     */
    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    /**
     * @return the classBuilder
     */
    public TypeSpec.Builder getClassBuilder() {
        return classBuilder;
    }

    /**
     * @param classBuilder the classBuilder to set
     */
    public void setClassBuilder(TypeSpec.Builder classBuilder) {
        this.classBuilder = classBuilder;
    }
}
