package com.cheji.web.context;

public class SourceContext {

    private static final ThreadLocal<String> CURRENT_SOURCE = new ThreadLocal<>();

    public static void setSource(String source) {
        CURRENT_SOURCE.set(source);
    }

    public static String getSource() {
        return CURRENT_SOURCE.get();
    }

    public static void clear() {
        CURRENT_SOURCE.remove();
    }
}
