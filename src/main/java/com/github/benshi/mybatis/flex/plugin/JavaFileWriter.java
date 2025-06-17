package com.github.benshi.mybatis.flex.plugin;

import java.io.File;
import java.io.IOException;

import com.squareup.javapoet.JavaFile;

public class JavaFileWriter {
    private JavaFileWriter() {
        // Prevent instantiation
    }

    public static void writeJavaFile(JavaFile javaFile, String sourceDir) throws IOException {
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
        javaFile.writeTo(directory);
    }
}
