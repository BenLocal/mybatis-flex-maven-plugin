package com.github.benshi.mybatis.flex.plugin.generator.impl;

import javax.lang.model.element.Modifier;

import com.github.benshi.mybatis.flex.plugin.generator.IPoetGenerator;
import com.mybatisflex.codegen.config.EntityConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.PackageConfig;
import com.mybatisflex.codegen.entity.Table;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
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
            System.out.printf("Entity class %s generated successfully.%n", javaFile.toString());
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

        TypeSpec entityTypeSpec = TypeSpec.classBuilder(entityName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(
                        com.mybatisflex.annotation.Table.class)
                        .addMember("value", "$S", table.getName())
                        .build())
                .build();

        return JavaFile.builder(
                packageConfig.getBasePackage(), entityTypeSpec)
                .build();
    }
}
