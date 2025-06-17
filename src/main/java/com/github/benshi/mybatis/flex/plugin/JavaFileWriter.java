package com.github.benshi.mybatis.flex.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.squareup.javapoet.JavaFile;

public class JavaFileWriter {
    private JavaFileWriter() {
        // Prevent instantiation
    }

    public static void writeJavaFile(JavaFile javaFile, String sourceDir, boolean rewrite) throws IOException {
        if (javaFile == null || sourceDir == null || sourceDir.isEmpty()) {
            throw new IllegalArgumentException("JavaFile and sourceDir must not be null or empty");
        }
        File directory = new File(sourceDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + sourceDir);
            }
        } else if (!directory.isDirectory()) {
            throw new IOException("Source path is not a directory: " + sourceDir);
        }

        if (!rewrite) {
            Path sourcePath = getSourcePath(javaFile, directory);
            if (sourcePath.toFile().exists()) {
                System.out.printf("File %s already exists, skipping.%n", sourcePath);
                return;
            }
        }
        javaFile.writeTo(directory);
    }

    private static Path getSourcePath(JavaFile javaFile, File directory) {
        Path outputDirectory = directory.toPath();
        if (!javaFile.packageName.isEmpty()) {
            for (String packageComponent : javaFile.packageName.split("\\.")) {
                outputDirectory = outputDirectory.resolve(packageComponent);
            }
        }

        return outputDirectory.resolve(javaFile.typeSpec.name + ".java");
    }
}
