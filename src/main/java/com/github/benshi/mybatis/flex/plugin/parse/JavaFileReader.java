package com.github.benshi.mybatis.flex.plugin.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.benshi.mybatis.flex.plugin.JavaFileUtils;
import com.github.benshi.mybatis.flex.plugin.LogHolder;

/**
 * 
 * 
 * @date 2025年6月18日
 * @time 22:43:57
 * @description
 * 
 */
public class JavaFileReader {
    private JavaFileReader() {
        // Prevent instantiation
    }

    public static ParsedJavaFile readJavaFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }

        String sourceCode = JavaFileUtils.readStringFromFile(path);
        return parseJavaSource(sourceCode, filePath);
    }

    public static ParsedJavaFile parseJavaSource(String sourceCode, String fileName) {
        JavaParser parser = new JavaParser();
        ParsedJavaFile result = new ParsedJavaFile();

        try {
            Optional<CompilationUnit> parseResult = parser.parse(sourceCode).getResult();

            if (parseResult.isPresent()) {
                CompilationUnit cu = parseResult.get();

                // 解析包名
                cu.getPackageDeclaration().ifPresent(pkg -> result.setPackageName(pkg.getNameAsString()));

                // 解析import语句
                cu.getImports().forEach(imp -> result.getImports().add(imp.getNameAsString()));

                // 使用访问者模式解析类内容
                new JavaFileVisitor(result).visit(cu, null);

                LogHolder.info("Successfully parsed Java file: " + fileName);
            } else {
                LogHolder.error("Failed to parse Java file: " + fileName);
            }
        } catch (Exception e) {
            LogHolder.error("Error parsing Java file: " + e.getMessage());
        }

        return result;
    }
}
