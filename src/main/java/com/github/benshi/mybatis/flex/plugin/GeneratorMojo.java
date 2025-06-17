package com.github.benshi.mybatis.flex.plugin;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.benshi.mybatis.flex.plugin.generator.PoetGenerator;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.entity.Table;
import com.zaxxer.hikari.HikariDataSource;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GeneratorMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(property = "flex.gen.skip", defaultValue = "false")
    private boolean skip;

    @Parameter
    private GeneratorConfig config;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Skipping MyBatis-Flex code generation (flex.gen.skip = true)");
            return;
        }

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(config.getJdbcUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());

        GlobalConfig globalConfig = createGlobalConfig();

        Generator generator = new PoetGenerator(dataSource, globalConfig);
        List<Table> tables = generator.getTables();
        if (tables.isEmpty()) {
            getLog().warn("No tables found in the database. Please check your configuration.");
            return;
        }

        generator.generate(tables);

    }

    private GlobalConfig createGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSourceDir(project.getBasedir().getAbsolutePath() + "/src/main/java");
        globalConfig.setBasePackage(config.getPackageName());

        if (config.getIncludeTables() != null &&
                !config.getIncludeTables().isEmpty()) {
            globalConfig.setGenerateTables(config.getIncludeTables());
        }
        if (config.getExcludeTables() != null &&
                !config.getExcludeTables().isEmpty()) {
            globalConfig.setUnGenerateTables(config.getExcludeTables());
        }
        globalConfig.enableEntity()
                .setWithBaseClassEnable(true)
                .setBaseOverwriteEnable(true)
                .setAlwaysGenColumnAnnotation(true)
                .setOverwriteEnable(false)
                .setWithLombok(false)
                .setLombokNoArgsConstructorEnable(false)
                .setLombokAllArgsConstructorEnable(false)
                .setWithActiveRecord(false);

        if (config.isGenerateMapper()) {
            globalConfig.enableMapper()
                    .setMapperAnnotation(true);

            globalConfig.enableMapperXml();
        }

        return globalConfig;
    }

}
