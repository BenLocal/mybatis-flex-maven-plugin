package com.github.benshi.mybatis.flex.plugin.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.github.benshi.mybatis.flex.plugin.LogHolder;
import com.github.benshi.mybatis.flex.plugin.generator.impl.EntityGenerator;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.dialect.IDialect;
import com.mybatisflex.codegen.entity.Table;

public class PoetGenerator extends Generator {

    public PoetGenerator(DataSource dataSource, GlobalConfig globalConfig) {
        super(dataSource, globalConfig);
    }

    public PoetGenerator(DataSource dataSource, GlobalConfig globalConfig, IDialect dialect) {
        super(dataSource, globalConfig, dialect);
    }

    private static final List<IPoetGenerator> generators = new ArrayList<>();

    static {
        generators.add(new EntityGenerator());
    }

    @Override
    public void generate(List<Table> tables) {
        try {
            generateWithError(tables);
        } catch (IOException e) {
            LogHolder.error("Error generating code: " + e.getMessage());
            throw new RuntimeException("Code generation failed", e);
        }
    }

    public void generateWithError(List<Table> tables) throws IOException {
        if (tables == null || tables.isEmpty()) {
            LogHolder.error(String.format("table %s not found.%n", globalConfig.getGenerateTables()));
            return;
        } else {
            LogHolder.info(String.format("find tables: %s%n",
                    tables.stream().map(Table::getName).collect(Collectors.toSet())));
        }

        for (Table table : tables) {
            for (IPoetGenerator generator : generators) {
                generator.generate(table, globalConfig);
            }
        }
        LogHolder.info("Code is generated successfully.");
    }
}
