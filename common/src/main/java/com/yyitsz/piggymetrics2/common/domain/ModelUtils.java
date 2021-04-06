package com.yyitsz.piggymetrics2.common.domain;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public final class ModelUtils {

    private List<String> defaultIgnorePropertyList = new ArrayList<>();

    public static ModelUtils DEFAULT = new ModelUtils();

    public static ModelUtils BASE_MODEL = new ModelUtils(BaseModel.class);

    public ModelUtils() {
    }

    public ModelUtils(String... ignoreProperties) {
        defaultIgnorePropertyList = Arrays.asList(ignoreProperties);
    }

    public ModelUtils(Class... ignoreClasses) {
        if (ignoreClasses != null) {
            for (Class cls : ignoreClasses) {
                PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(cls);
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (propertyDescriptor.getWriteMethod() != null && propertyDescriptor.getReadMethod() != null) {
                        defaultIgnorePropertyList.add(propertyDescriptor.getName());
                    }
                }
            }
        }
    }

    public <T> void copy(List<T> sourceList,
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

        copy(sourceList, targetList, keyFunc, keyFunc, newFunc, ignoreProperties);
    }

    public <T1, T2> void copy(List<T1> sourceList,
                              List<T2> targetList,
                              Function<T1, Key> key1Func,
                              Function<T2, Key> key2Func,
                              Function<T1, T2> newFunc,
                              String... ignoreProperties) {
        List<String> allIgnoreProperties = new ArrayList<>(Arrays.asList(ignoreProperties));
        allIgnoreProperties.addAll(defaultIgnorePropertyList);

        if (sourceList == null || sourceList.isEmpty()) {
            targetList.clear();
            return;
        }

        for (Iterator<T2> iterator = targetList.iterator(); iterator.hasNext(); ) {
            T2 target = iterator.next();
            T1 found = null;
            for (T1 source : sourceList) {
                if (key1Func.apply(source).equals(key2Func.apply(target))) {
                    found = source;
                    break;
                }
            }
            if (found != null) {
                copy(found, target, allIgnoreProperties);
            } else {
                iterator.remove();
            }
        }
        List<T2> newList = new ArrayList<>();
        for (T1 source : sourceList) {
            T2 found = null;
            for (T2 target : targetList) {
                if (key1Func.apply(source).equals(key2Func.apply(target))) {
                    found = target;
                    break;
                }
            }
            if (found == null) {
                T2 target = newFunc.apply(source);
                copy(source, target, allIgnoreProperties);
                newList.add(target);
            }
        }
        targetList.addAll(newList);
    }

    public static <T> void copy(T source, T target, List<String> ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties.toArray(new String[0]));
    }

    public static <T> void copy(T source, T target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }
}
