package com.github.benshi.mybatis.flex.plugin.parse;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * 
 * 
 * @date 2025年6月18日
 * @time 22:48:02
 * @description
 * 
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
    private final String code;

    public StringJavaFileObject(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
