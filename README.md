# mybatis-flex-maven-plugin

[![](https://jitpack.io/v/benlocal/mybatis-flex-maven-plugin.svg?style=flat-square)](https://jitpack.io/#benlocal/mybatis-flex-maven-plugin)
![Weekly download statistics](https://jitpack.io/v/benlocal/mybatis-flex-maven-plugin/week.svg)
![Monthly download statistics](https://jitpack.io/v/benlocal/mybatis-flex-maven-plugin/month.svg)

## Introduction

mybatis-flex-maven-plugin is a Maven code generation plugin based on the MyBatis-Flex framework, designed to automatically generate database entity classes, Mapper interfaces, and other code files. This plugin significantly simplifies the development process and improves development efficiency.

### Key Features

- **Automatic Code Generation**: Automatically generates corresponding entity classes based on database table structures
- **Java Keyword Handling**: Automatically handles Java keyword conflicts to avoid compilation errors
- **Nullable Field Optimization**: Automatically converts nullable fields to `java.util.Optional<T>` types
- **Flexible Configuration**: Supports include/exclude configuration for specific tables
- **Maven Integration**: Seamlessly integrates into the Maven build lifecycle

### Core Functionality

1. **Smart Property Renaming**: Automatically renames properties when database column names are Java keywords or start with numbers
2. **Optional Type Support**: Generates Optional wrapper types for nullable fields, providing better null value handling
3. **Custom Type Handlers**: Integrates with mybatis-helper library to provide TypeHandlers for Optional types
4. **Batch Table Processing**: Supports simultaneous code generation for multiple database tables

### Use Cases

- Quickly scaffold projects based on MyBatis-Flex
- Automate database-to-Java entity mapping
- Need safer null value handling (using Optional)
- Avoid Java keyword conflicts

### Used

1. Add the JitPack repository to your build file

```xml
    <pluginRepositories>
        <pluginRepository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </pluginRepository>
    </pluginRepositories>
```

2. Add the plugins

- mybatis-flex-maven-plugin

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.benlocal</groupId>
            <artifactId>mybatis-flex-maven-plugin</artifactId>
            <version>main-SNAPSHOT</version>
            <dependencies>
                <dependency>
                    <groupId>com.mysql</groupId>
                    <artifactId>mysql-connector-j</artifactId>
                    <version>8.3.0</version>
                </dependency>
            </dependencies>
            <configuration>
                <config>
                    <jdbcUrl>jdbc:mysql://localhost:3306/worker</jdbcUrl>
                    <username>root</username>
                    <password>root</password>
                    <packageName>com.example.test.model</packageName>
                </config>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

# jsr305 dependency (only for java8)

```xml
<dependency>
    <groupId>com.google.code.findbugs</groupId>
    <artifactId>jsr305</artifactId>
    <version>3.0.0</version>
</dependency>
```

### Configuration Options

| Parameter        | Type           | Default | Description                                    |
| ---------------- | -------------- | ------- | ---------------------------------------------- |
| `jdbcUrl`        | `String`       | -       | Database JDBC URL                              |
| `username`       | `String`       | -       | Database username                              |
| `password`       | `String`       | -       | Database password                              |
| `packageName`    | `String`       | -       | Target package name for generated classes      |
| `includeTables`  | `List<String>` | -       | List of tables to include in code generation   |
| `excludeTables`  | `List<String>` | -       | List of tables to exclude from code generation |
| `generateMapper` | `boolean`      | false   | Whether to generate Mapper interfaces          |

### Skip Plugin Execution

You can skip the plugin execution in several ways:

1. **Using properties in pom.xml:**

```xml
<properties>
    <flex.gen.skip>true</flex.gen.skip>
</properties>
```

2. **Using command line:**

```bash
mvn compile -Dflex.gen.skip=true
```

3. **Using profiles:**

```xml
<profiles>
    <profile>
        <id>skip-generation</id>
        <properties>
            <flex.gen.skip>true</flex.gen.skip>
        </properties>
    </profile>
</profiles>
```

Then activate the profile:

```bash
mvn compile -Pskip-generation
```
