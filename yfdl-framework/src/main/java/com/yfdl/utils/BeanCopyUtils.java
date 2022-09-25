package com.yfdl.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {


    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> clazz)  {
        //创建目标对象

        V result = null;
        try {
            result = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        BeanUtils.copyProperties(source,result);
        return result;
        //返回结果
    }

    public static <V,O> List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.stream().map(o-> copyBean(o,clazz)).collect(Collectors.toList());
    }

}
