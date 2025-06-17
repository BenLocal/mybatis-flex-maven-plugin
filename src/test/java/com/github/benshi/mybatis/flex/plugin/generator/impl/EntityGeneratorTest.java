package com.github.benshi.mybatis.flex.plugin.generator.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.GlobalConfig.FileType;
import com.mybatisflex.codegen.entity.Column;
import com.mybatisflex.codegen.entity.Table;

public class EntityGeneratorTest {

    private GlobalConfig getDefaultGlobalConfig(boolean withBaseClass, String sourceDir) {
        GlobalConfig globalConfig = new GlobalConfig(FileType.JAVA);
        globalConfig.setBasePackage("com.github.benshi.mybatis.flex.plugin.generator.impl");
        globalConfig.setSourceDir(sourceDir);
        globalConfig.enableEntity()
                .setOverwriteEnable(false)
                .setBaseOverwriteEnable(true)
                .setWithBaseClassEnable(withBaseClass);

        return globalConfig;
    }

    private Table getDefaultTable(GlobalConfig globalConfig) {
        Table table = new Table();
        table.setName("test_table");
        table.setComment("Test Tabl\n11111");

        List<Column> columns = new ArrayList<>();
        Column column1 = new Column();
        column1.setColumnConfig(new ColumnConfig());
        column1.setName("id");
        column1.setComment("Primary Key");
        column1.setRawType("int");
        column1.setPrimaryKey(true);
        column1.setAutoIncrement(true);
        column1.setPropertyType("java.lang.Integer");
        columns.add(column1);
        Column column2 = new Column();
        column2.setColumnConfig(new ColumnConfig());
        column2.setName("name");
        column2.setComment("Name Column");
        column2.setRawType("varchar");
        column2.setPropertyType("java.lang.String");
        column2.setNullable(1);
        columns.add(column2);
        Column column3 = new Column();
        column3.setColumnConfig(new ColumnConfig());
        column3.setName("1to1");
        column3.setComment("1to1 Column");
        column3.setRawType("varchar");
        column3.setPropertyType("java.lang.String");
        column3.setNullable(1);
        columns.add(column3);
        Column column4 = new Column();
        column4.setColumnConfig(new ColumnConfig());
        column4.setName("switch");
        column4.setComment("switch Column");
        column4.setRawType("varchar");
        column4.setPropertyType("java.lang.String");
        column4.setNullable(1);
        columns.add(column4);
        table.setColumns(columns);
        table.setGlobalConfig(globalConfig);
        return table;
    }

    private void deleteDirectory(String dirPath) {
        try {
            Path path = Paths.get(dirPath);
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (Exception e) {
            System.err.println("Failed to delete directory: " + dirPath + ", error: " + e.getMessage());
        }
    }

    @Test
    void testGenerateWithBaseClassEnable() throws IOException {
        GlobalConfig globalConfig = getDefaultGlobalConfig(true,
                "src/test/resources/generated-sources");
        Table table = getDefaultTable(globalConfig);
        EntityGenerator g = new EntityGenerator();
        g.generate(table, globalConfig);

        // Verify the generated entity class
        String expectedFilePath = "src/test/resources/generated-sources/com/github/benshi/mybatis/flex/plugin/generator/impl/TestTable.java";
        String baseFilePath = "src/test/resources/generated-sources/com/github/benshi/mybatis/flex/plugin/generator/impl/base/TestTableBase.java";
        assertTrue(
                new File(baseFilePath).exists(),
                "Base class file does not exist: " + baseFilePath);
        assertTrue(
                new File(expectedFilePath).exists(),
                "Generated file does not exist: " + expectedFilePath);
        // clear generated files
        // delete src/test/resources/generated-sources directory
        // Clear generated files - 改进的删除逻辑
        deleteDirectory("src/test/resources/generated-sources");
    }

    @Test
    void testGenerateWithBaseClassDisable() throws IOException {
        GlobalConfig globalConfig = getDefaultGlobalConfig(false,
                "src/test/resources/generated-sources");
        Table table = getDefaultTable(globalConfig);
        EntityGenerator g = new EntityGenerator();
        g.generate(table, globalConfig);

        // Verify the generated entity class
        deleteDirectory("src/test/resources/generated-sources");
    }
}
