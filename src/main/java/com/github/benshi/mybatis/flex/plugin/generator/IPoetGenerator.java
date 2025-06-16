package com.github.benshi.mybatis.flex.plugin.generator;

import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.entity.Table;

public interface IPoetGenerator {
    void generate(Table table, GlobalConfig config);
}
