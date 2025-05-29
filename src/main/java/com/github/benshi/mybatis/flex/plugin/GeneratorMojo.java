package com.github.benshi.mybatis.flex.plugin;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.benshi.mybatis.handler.OptionalTypeHandlerFactory;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.entity.Column;
import com.mybatisflex.codegen.entity.Table;
import com.zaxxer.hikari.HikariDataSource;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GeneratorMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter
    private GeneratorConfig config;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(config.getJdbcUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());

        GlobalConfig globalConfig = createGlobalConfig();

        Generator generator = new Generator(dataSource, globalConfig);
        List<Table> tables = generator.getTables();
        if (tables.isEmpty()) {
            getLog().warn("No tables found in the database. Please check your configuration.");
            return;
        }

        resetTables(tables);

        generator.generate(tables);

    }

    private void resetTables(List<Table> tables) {
        for (Table table : tables) {
            if (table.getColumns() == null || table.getColumns().isEmpty()) {
                continue;
            }

            for (Column column : table.getColumns()) {
                // Reset the property name to the original column name
                resetJavaProperties(table, column);
                // Set the column as optional if it is nullable
                setOptionalProperties(table, column);
            }
        }
    }

    private void resetJavaProperties(Table table, Column column) {
        String columnName = column.getProperty();
        if (JavaKeywordsUtils.isJavaKeyword(columnName) ||
                JavaKeywordsUtils.startWithNumberKeyword(columnName)) {
            // If the column name is a Java keyword or starts with a number,
            column.setProperty(renameProperty(columnName));
            getLog().warn(String.format(
                    "Column '%s' in table '%s' is renamed to '%s' to avoid conflicts with Java keywords.",
                    columnName, table.getName(), column.getProperty()));
        }
    }

    private void setOptionalProperties(Table table, Column column) {
        String columnName = column.getProperty();
        if (column.getNullable() != null && column.getNullable() == 1) {
            getLog().info(String.format(
                    "Column '%s' in table '%s' is nullable, changing type to java.util.Optional<%s>.",
                    columnName, table.getName(), column.getPropertyType()));

            if (column.getColumnConfig() == null) {
                ColumnConfig cc = new ColumnConfig()
                        .setTypeHandler(
                                OptionalTypeHandlerFactory.getInstance().getTypeHandlerClazz(column.getPropertyType()));
                // ColumnConfig cc = new ColumnConfig()
                // .setTypeHandler(
                // OptionalTypeHandler.class);
                column.setColumnConfig(cc);
            } else {
                ColumnConfig cc = column.getColumnConfig();
                if (cc.getTypeHandler() == null) {
                    cc.setTypeHandler(
                            OptionalTypeHandlerFactory.getInstance().getTypeHandlerClazz(column.getPropertyType()));
                    // cc.setTypeHandler(OptionalTypeHandler.class);
                }
            }

            // Change the property type to Optional<>
            column.setPropertyType("java.util.Optional<" + column.getPropertyType() + ">");
        }
    }

    private String renameProperty(String property) {
        if (property == null || property.isEmpty()) {
            return property;
        }

        String tmp = property.trim();
        if (Character.isLowerCase(property.charAt(0))) {
            tmp = Character.toUpperCase(property.charAt(0)) + property.substring(1);
        }

        return String.format("valueOf%s", tmp);
    }

    private GlobalConfig createGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
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
                .setWithLombok(false)
                .setWithBaseClassEnable(true)
                .setBaseOverwriteEnable(true)
                .setAlwaysGenColumnAnnotation(true)
                .setOverwriteEnable(false)
                .setLombokNoArgsConstructorEnable(false)
                .setLombokNoArgsConstructorEnable(false);

        return globalConfig;
    }

}
