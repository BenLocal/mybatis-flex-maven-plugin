package com.github.benshi.mybatis.flex.plugin.generator;

import java.io.IOException;

import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.entity.Table;

public interface IPoetGenerator {
    void generate(Table table, GlobalConfig config) throws IOException;
}
