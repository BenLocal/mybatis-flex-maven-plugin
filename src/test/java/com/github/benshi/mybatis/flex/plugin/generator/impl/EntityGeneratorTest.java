package com.github.benshi.mybatis.flex.plugin.generator.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.GlobalConfig.FileType;
import com.mybatisflex.codegen.entity.Column;
import com.mybatisflex.codegen.entity.Table;

public class EntityGeneratorTest {

    @Test
    void testGenerate() {
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
        table.setColumns(columns);
        table.setColumns(columns);

        GlobalConfig globalConfig = new GlobalConfig(FileType.JAVA);
        globalConfig.setBasePackage("com.example");
        globalConfig.enableEntity()
                .setWithBaseClassEnable(true);

        table.setGlobalConfig(globalConfig);

        EntityGenerator g = new EntityGenerator();
        g.generate(table, globalConfig);
    }
}
