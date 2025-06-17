package com.github.benshi.mybatis.flex.plugin;

public class ColumnUtils {
    private ColumnUtils() {
        // Prevent instantiation
    }

    public static String getColumnName(String columnName) {
        if (columnName == null || columnName.isEmpty()) {
            return "";
        }
        if (JavaKeywordsUtils.isJavaKeyword(columnName) ||
                JavaKeywordsUtils.startWithNumberKeyword(columnName)) {
            return renameProperty(columnName);
        }
        return columnName.trim();
    }

    private static String renameProperty(String property) {
        String tmp = property.trim();
        if (Character.isLowerCase(property.charAt(0))) {
            tmp = Character.toUpperCase(property.charAt(0)) + property.substring(1);
        }

        return String.format("valueOf%s", tmp);
    }
}
