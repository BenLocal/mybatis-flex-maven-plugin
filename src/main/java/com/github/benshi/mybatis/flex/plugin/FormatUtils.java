package com.github.benshi.mybatis.flex.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.palantir.javaformat.java.Formatter;
import com.palantir.javaformat.java.FormatterException;
import com.palantir.javaformat.java.JavaFormatterOptions;
import com.palantir.javaformat.java.JavaFormatterOptions.Style;

public class FormatUtils {
    private FormatUtils() {
        // Prevent instantiation
    }

    public static void formatGeneratedFiles(MavenProject project, GeneratorConfig config)
            throws MojoExecutionException {
        try {
            String sourceDir = project.getBasedir().getAbsolutePath() + "/src/main/java";
            String packagePath = config.getPackageName().replace('.', '/');
            Path targetPackagePath = Paths.get(sourceDir, packagePath);

            if (!Files.exists(targetPackagePath)) {
                return;
            }

            JavaFormatterOptions options = JavaFormatterOptions.builder()
                    .style(Style.AOSP)
                    .build();
            Formatter formatter = Formatter.createFormatter(options);

            // 格式化所有Java文件
            try (Stream<Path> paths = Files.walk(targetPackagePath)) {
                paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java"))
                        .forEach(path -> formatJavaFile(path, formatter));
            }

        } catch (IOException e) {
            throw new MojoExecutionException("Failed to format generated files", e);
        }
    }

    private static void formatJavaFile(Path javaFile, Formatter formatter) {
        try {
            String originalContent = JavaFileUtils.readStringFromFile(javaFile);

            String formattedContent = formatter.formatSource(originalContent);

            if (!originalContent.equals(formattedContent)) {
                JavaFileUtils.writeStringToFile(javaFile, formattedContent);
            }

        } catch (IOException | FormatterException e) {
            LogHolder.error("Failed to format file: " + javaFile + ", error: " + e.getMessage());
        }
    }

}
