package com.github.benshi.mybatis.flex.plugin.generator.impl;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

import com.github.benshi.mybatis.flex.plugin.generator.IPoetGenerator;
import com.mybatisflex.codegen.config.EntityConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.PackageConfig;
import com.mybatisflex.codegen.entity.Column;
import com.mybatisflex.codegen.entity.Table;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

public class EntityGenerator implements IPoetGenerator {

    @Override
    public void generate(Table table, GlobalConfig config) {
        if (!config.isEntityGenerateEnable()) {
            return;
        }

        genEntity(table, config);
        genBaseClass(table, config);
    }

    private void genEntity(Table table, GlobalConfig globalConfig) {
        JavaFile javaFile = createJavaFile(table, globalConfig);
        if (javaFile != null) {
            System.out.printf("Entity class generated successfully:\n%s\n", javaFile.toString());
        } else {
            System.err.printf("Failed to generate entity class for table %s.%n", table.getName());
        }
    }

    private void genBaseClass(Table table, GlobalConfig globalConfig) {
        EntityConfig entityConfig = globalConfig.getEntityConfig();
        if (!entityConfig.isWithBaseClassEnable()) {
            return;
        }
    }

    private JavaFile createJavaFile(Table table, GlobalConfig globalConfig) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();
        String entityName = table.getEntityJavaFileName();

        TypeSpec.Builder entityTypeSpecBuilder = TypeSpec.classBuilder(entityName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(
                        com.mybatisflex.annotation.Table.class)
                        .addMember("value", "$S", table.getName())
                        .build())
                .addSuperinterface(Serializable.class);

        entityTypeSpecBuilder.addJavadoc("Entity class for table: $L\n", table.getName());
        entityTypeSpecBuilder.addJavadoc("\n");
        entityTypeSpecBuilder.addJavadoc("<pre>\n");
        entityTypeSpecBuilder.addJavadoc("$L\n", table.getComment());
        entityTypeSpecBuilder.addJavadoc("</pre>\n");
        entityTypeSpecBuilder.addField(
                FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$LL", 1)
                        .build());

        for (Column column : table.getColumns()) {
            ClassName columnType = ClassName.bestGuess(column.getPropertyType());

            FieldSpec.Builder field = FieldSpec.builder(columnType, column.getProperty(), Modifier.PRIVATE);

            String methodName = firstCharToUpperCase(column.getProperty());
            MethodSpec.Builder getterMethod = MethodSpec.methodBuilder("get" + methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(columnType)
                    .addStatement("return $N", column.getProperty())
                    .addJavadoc("Gets the value of the $L property.\n", column.getProperty())
                    .addJavadoc("\n")
                    .addJavadoc("<pre>\n")
                    .addJavadoc("$L\n", column.getComment())
                    .addJavadoc("</pre>\n")
                    .addJavadoc("\n")
                    .addJavadoc("@return the value of the property\n");

            if (column.getNullable() != null && column.getNullable() == 1) {
                getterMethod.addAnnotation(AnnotationSpec.builder(Nullable.class).build());
            }

            MethodSpec.Builder setterMethod = MethodSpec.methodBuilder("set" + methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(columnType, column.getProperty())
                    .addStatement("this.$N = $N", column.getProperty(), column.getProperty())
                    .addJavadoc("Sets the value of the $L property.\n", column.getProperty())
                    .addJavadoc("\n")
                    .addJavadoc("<pre>\n")
                    .addJavadoc("$L\n", column.getComment())
                    .addJavadoc("</pre>\n")
                    .addJavadoc("\n")
                    .addJavadoc("@param $N the new value for the property\n", column.getProperty());

            ParameterizedTypeName optionalType = ParameterizedTypeName.get(
                    ClassName.get(java.util.Optional.class),
                    columnType);
            MethodSpec.Builder optionalMethod = MethodSpec.methodBuilder("get" + methodName + "WithOptional")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(optionalType)
                    .addStatement("return Optional.ofNullable($N)", column.getProperty())
                    .addJavadoc("Gets the value of the $L property wrapped in an Optional.\n", column.getProperty())
                    .addJavadoc("\n")
                    .addJavadoc("<pre>\n")
                    .addJavadoc("$L\n", column.getComment())
                    .addJavadoc("</pre>\n")
                    .addJavadoc("\n")
                    .addJavadoc("@return an Optional containing the value of the property, or empty if null\n")
                    .addAnnotation(
                            AnnotationSpec.builder(
                                    Nonnull.class)
                                    .build());

            entityTypeSpecBuilder.addField(field.build());
            entityTypeSpecBuilder.addMethod(getterMethod.build());
            entityTypeSpecBuilder.addMethod(setterMethod.build());
            entityTypeSpecBuilder.addMethod(optionalMethod.build());
        }

        return JavaFile.builder(
                packageConfig.getBasePackage(), entityTypeSpecBuilder.build())
                .build();
    }

    private String firstCharToUpperCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
