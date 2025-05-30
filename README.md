# mybatis-flex-maven-plugin

mybatis-flex-maven-plugin

[![](https://jitpack.io/v/benlocal/mybatis-flex-maven-plugin.svg?style=flat-square)](https://jitpack.io/#benlocal/mybatis-flex-maven-plugin)
![Weekly download statistics](https://jitpack.io/v/benlocal/mybatis-flex-maven-plugin/week.svg)
![Monthly download statistics](https://jitpack.io/v/benlocal/mybatis-flex-maven-plugin/month.svg)

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
