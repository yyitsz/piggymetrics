package com.yyitsz.piggymetrics2.common.domain;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public final class ModelUtils {
    public static <T> void copy(T source, T target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    public static <T> void copy(List<T> sourceList,
                                List<T> targetList,
                                Function<T, Key> keyFunc,
                                Function<T, T> newFunc,
                                String... ignoreProperties) {
        if (sourceList == null || sourceList.isEmpty()) {
            targetList.clear();
            return;
        }
        if (targetList.isEmpty()) {
            targetList.addAll(sourceList);
            return;
        }
        for (Iterator<T> iterator = targetList.iterator(); iterator.hasNext(); ) {
            T target = iterator.next();
            T found = null;
            for (T source : sourceList) {
                if (keyFunc.apply(source).equals(keyFunc.apply(target))) {
                    found = target;
                    break;
                }
            }
            if (found != null) {
                copy(found, target, ignoreProperties);
            } else {
                iterator.remove();
            }
        }
        List<T> newList = new ArrayList<>();
        for (T source : sourceList) {
            T found = null;
            for (T target : targetList) {
                if (keyFunc.apply(source).equals(keyFunc.apply(target))) {
                    found = target;
                    break;
                }
            }
            if (found == null) {
                T target = newFunc.apply(source);
                copy(source, target, ignoreProperties);
                newList.add(target);
            }
        }
        targetList.addAll(newList);
    }
}
