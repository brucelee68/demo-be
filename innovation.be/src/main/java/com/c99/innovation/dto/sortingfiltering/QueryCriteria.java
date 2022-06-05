package com.c99.innovation.dto.sortingfiltering;

public class QueryCriteria {

    private String key;

    private Object value;

    public QueryCriteria(String fieldName, Object value) {
        this.setKey(fieldName);
        this.setValue(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
