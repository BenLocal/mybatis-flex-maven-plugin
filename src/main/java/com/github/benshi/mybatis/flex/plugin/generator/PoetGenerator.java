package com.github.benshi.mybatis.flex.plugin.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.github.benshi.mybatis.flex.plugin.generator.impl.EntityGenerator;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.entity.Table;

public class PoetGenerator extends Generator {
    public PoetGenerator(DataSource dataSource, GlobalConfig globalConfig) {
        super(dataSource, globalConfig);
    }

    private static final List<IPoetGenerator> generators = new ArrayList<>();

    static {
        generators.add(new EntityGenerator());
    }

    @Override
    public void generate(List<Table> tables) {
        if (tables == null || tables.isEmpty()) {
            System.err.printf("table %s not found.%n", globalConfig.getGenerateTables());
            return;
        } else {
            System.out.printf("find tables: %s%n", tables.stream().map(Table::getName).collect(Collectors.toSet()));
        }

        for (Table table : tables) {
            for (IPoetGenerator generator : generators) {
                generator.generate(table, globalConfig);
            }
        }
        System.out.println("Code is generated successfully.");
    }
}
