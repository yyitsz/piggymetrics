package com.yyitsz.piggymetrics2.common.domain;

import java.util.Arrays;

public class Key {
    private Object[] keyList = new Object[0];

    public Key(Object... keys) {
        keyList = keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return Arrays.equals(keyList, key.keyList);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keyList);
    }
}
