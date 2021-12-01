package com.bs.search.common;

public enum ApiEnum {

    TARGET_KEY("target"),
    TARGET_VALUE("title"),
    QUERY_KEY("query"),
    QUERY_VALUE("카카오");

    private final String value;

    ApiEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
