package com.github.benshi.mybatis.flex.plugin;

import java.util.HashSet;
import java.util.Set;

import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.sisu.Parameters;

public class GeneratorConfig {
    @Parameter(required = true)
    private String jdbcUrl;

    @Parameter(required = true)
    private String username;

    @Parameter(required = true)
    private String password;

    @Parameter(required = true)
    private String packageName;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/java")
    private String outputDirectory;

    @Parameter
    private Set<String> includeTables = new HashSet<>();

    @Parameter
    private Set<String> excludeTables = new HashSet<>();

    @Parameters
    private boolean generateMapper;

    /**
     * @return the generateMapper
     */
    public boolean isGenerateMapper() {
        return generateMapper;
    }

    /**
     * @param generateMapper the generateMapper to set
     */
    public void setGenerateMapper(boolean generateMapper) {
        this.generateMapper = generateMapper;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Set<String> getIncludeTables() {
        return includeTables;
    }

    public void setIncludeTables(Set<String> includeTables) {
        this.includeTables = includeTables;
    }

    public Set<String> getExcludeTables() {
        return excludeTables;
    }

    public void setExcludeTables(Set<String> excludeTables) {
        this.excludeTables = excludeTables;
    }
}
