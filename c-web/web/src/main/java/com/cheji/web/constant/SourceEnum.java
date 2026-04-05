package com.cheji.web.constant;

public enum SourceEnum {
    SSP("SSP", "千里眼"),
    TBD_2("TBD_2", "小程序2"),
    TBD_3("TBD_3", "小程序3"),
    TBD_4("TBD_4", "小程序4");

    private final String code;
    private final String name;

    SourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SourceEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (SourceEnum source : values()) {
            if (source.code.equals(code)) {
                return source;
            }
        }
        return null;
    }

    public static boolean isValid(String code) {
        return fromCode(code) != null;
    }
}
