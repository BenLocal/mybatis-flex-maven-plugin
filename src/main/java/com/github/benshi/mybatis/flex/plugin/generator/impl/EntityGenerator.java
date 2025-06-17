package com.github.benshi.mybatis.flex.plugin.generator.impl;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;

import com.github.benshi.mybatis.flex.plugin.ColumnUtils;
import com.github.benshi.mybatis.flex.plugin.JavaFileWriter;
import com.github.benshi.mybatis.flex.plugin.generator.IPoetGenerator;
import com.mybatisflex.annotation.KeyType;
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
    public void generate(Table table, GlobalConfig config) throws IOException {
        if (!config.isEntityGenerateEnable()) {
            return;
        }

        genEntity(table, config);
        genBaseClass(table, config);
    }

    private void genEntity(Table table, GlobalConfig globalConfig) throws IOException {
        EntityConfig entityConfig = globalConfig.getEntityConfig();
        JavaFile javaFile = null;
        if (!entityConfig.isWithBaseClassEnable()) {
            javaFile = createJavaFile(table, globalConfig, false);
        } else {
            // enable base class generation
            javaFile = createJavaFileWithBase(table, globalConfig);
        }

        if (javaFile != null) {
            boolean rewrite = entityConfig.isOverwriteEnable();
            JavaFileWriter.writeJavaFile(javaFile, globalConfig.getSourceDir(), rewrite);
        } else {
            System.err.printf("Failed to generate entity class for table %s.%n", table.getName());
        }
    }

    private void genBaseClass(Table table, GlobalConfig globalConfig) throws IOException {
        EntityConfig entityConfig = globalConfig.getEntityConfig();
        if (!entityConfig.isWithBaseClassEnable()) {
            return;
        }

        JavaFile javaFile = createJavaFile(table, globalConfig, true);
        if (javaFile != null) {
            boolean rewrite = entityConfig.isBaseOverwriteEnable();
            JavaFileWriter.writeJavaFile(javaFile, globalConfig.getSourceDir(), rewrite);
        } else {
            System.err.printf("Failed to generate base entity class for table %s.%n", table.getName());
        }
    }

    private String getBasePackageName(GlobalConfig globalConfig, boolean base) {
        PackageConfig packageConfig = globalConfig.getPackageConfig();

        if (base) {
            return String.format("%s.base", packageConfig.getBasePackage());
        }
        return packageConfig.getBasePackage();
    }

    private String getClassFileName(Table table, boolean base) {
        String entityName = table.getEntityJavaFileName();
        if (base) {
            return String.format("%sBase", entityName);
        }
        return entityName;
    }

    private JavaFile createJavaFileWithBase(Table table, GlobalConfig globalConfig) {
        String classFileName = getClassFileName(table, false);
        String classBaseFileName = getClassFileName(table, true);
        ClassName baseType = ClassName.bestGuess(getBasePackageName(globalConfig, true) + "." + classBaseFileName);
        TypeSpec.Builder entityTypeSpecBuilder = TypeSpec.classBuilder(classFileName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(com.mybatisflex.annotation.Table.class)
                        .addMember("value", "$S", table.getName())
                        .build())
                .superclass(baseType);

        // comment
        entityTypeSpecBuilder.addJavadoc("Entity class for table: $L\n", table.getName())
                .addJavadoc("\n");

        if (table.getComment() != null && !table.getComment().isEmpty()) {
            entityTypeSpecBuilder.addJavadoc("<pre>\n")
                    .addJavadoc("$L\n", table.getComment())
                    .addJavadoc("</pre>\n")
                    .addJavadoc("\n");
        }

        return JavaFile.builder(getBasePackageName(globalConfig, false), entityTypeSpecBuilder.build()).build();
    }

    private JavaFile createJavaFile(Table table, GlobalConfig globalConfig, boolean base) {
        String classFileName = getClassFileName(table, base);

        TypeSpec.Builder entityTypeSpecBuilder = TypeSpec.classBuilder(classFileName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Serializable.class);

        if (base) {
            entityTypeSpecBuilder.addAnnotation(AnnotationSpec.builder(com.mybatisflex.annotation.Table.class)
                    .addMember("value", "$S", table.getName())
                    .build());
        }

        entityTypeSpecBuilder.addJavadoc("Entity class for table: $L\n", table.getName())
                .addJavadoc("\n")
                .addField(FieldSpec.builder(long.class, "serialVersionUID", Modifier.PRIVATE, Modifier.STATIC,
                        Modifier.FINAL)
                        .initializer("$LL", 1)
                        .build());

        if (table.getComment() != null && !table.getComment().isEmpty()) {
            entityTypeSpecBuilder.addJavadoc("<pre>\n")
                    .addJavadoc("$L\n", table.getComment())
                    .addJavadoc("</pre>\n")
                    .addJavadoc("\n");
        }

        for (Column column : table.getColumns()) {
            ClassName columnType = ClassName.bestGuess(column.getPropertyType());
            String columnName = ColumnUtils.getColumnName(column.getProperty());

            FieldSpec.Builder field = FieldSpec
                    .builder(columnType, columnName, Modifier.PRIVATE);

            if (column.isPrimaryKey()) {
                field.addAnnotation(AnnotationSpec.builder(com.mybatisflex.annotation.Id.class)
                        .addMember("keyType", "$T.$L", KeyType.class, "Auto")
                        .build());
            }

            field.addAnnotation(AnnotationSpec.builder(com.mybatisflex.annotation.Column.class)
                    .addMember("value", "$S", column.getName())
                    .build());

            String methodName = firstCharToUpperCase(columnName);
            MethodSpec.Builder getterMethod = MethodSpec.methodBuilder("get" + methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(columnType)
                    .addStatement("return $N", columnName)
                    .addJavadoc("Gets the value of the $L property.\n", columnName)
                    .addJavadoc("\n");
            if (column.getComment() != null && !column.getComment().isEmpty()) {
                getterMethod.addJavadoc("<pre>\n")
                        .addJavadoc("$L\n", column.getComment())
                        .addJavadoc("</pre>\n")
                        .addJavadoc("\n");
            }
            getterMethod.addJavadoc("@return the value of the property\n");

            if (column.getNullable() != null && column.getNullable() == 1) {
                getterMethod.addAnnotation(AnnotationSpec.builder(Nullable.class).build());
            }

            MethodSpec.Builder setterMethod = MethodSpec.methodBuilder("set" + methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(columnType,
                            columnName)
                    .addStatement("this.$N = $N",
                            columnName,
                            columnName)
                    .addJavadoc("Sets the value of the $L property.\n",
                            columnName)
                    .addJavadoc("\n");

            if (column.getComment() != null && !column.getComment().isEmpty()) {
                setterMethod.addJavadoc("<pre>\n")
                        .addJavadoc("$L\n", column.getComment())
                        .addJavadoc("</pre>\n")
                        .addJavadoc("\n");
            }
            getterMethod.addJavadoc("@param $N the new value for the property\n", columnName);

            ParameterizedTypeName optionalType = ParameterizedTypeName.get(
                    ClassName.get(java.util.Optional.class),
                    columnType);
            MethodSpec.Builder optionalMethod = MethodSpec.methodBuilder("get" + methodName + "WithOptional")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(optionalType)
                    .addStatement("return Optional.ofNullable($N)",
                            columnName)
                    .addJavadoc("Gets the value of the $L property wrapped in an Optional.\n",
                            columnName)
                    .addJavadoc("\n")
                    .addAnnotation(
                            AnnotationSpec.builder(
                                    Nonnull.class)
                                    .build());

            if (column.getComment() != null && !column.getComment().isEmpty()) {
                optionalMethod.addJavadoc("<pre>\n")
                        .addJavadoc("$L\n", column.getComment())
                        .addJavadoc("</pre>\n")
                        .addJavadoc("\n");
            }
            optionalMethod.addJavadoc("@return an Optional containing the value of the property, or empty if null\n");

            entityTypeSpecBuilder.addField(field.build());
            entityTypeSpecBuilder.addMethod(getterMethod.build());
            entityTypeSpecBuilder.addMethod(setterMethod.build());
            entityTypeSpecBuilder.addMethod(optionalMethod.build());
        }

        return JavaFile.builder(getBasePackageName(globalConfig, base), entityTypeSpecBuilder.build()).build();
    }

    private String firstCharToUpperCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
