package com.change.hippo.utils.trans;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


public class BeanUtility {
    public static <T> T beanCopy(Object fromBean, Class<T> type) {
        T toBean = null;
        try {
            toBean = type.newInstance(); // 创建 JavaBean 对象
            if (fromBean == null || toBean == null) {
                return null;
            }
//            BeanCopier.create(fromBean.getClass(),toBean.getClass(),true);
            BeanUtils.copyProperties(fromBean, toBean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return toBean;
    }

    public static <T,E>  List<T> convertOtherBeanList(List<E> list, Class<T> type) {
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<T> resultList = new ArrayList<>();
        for(E e : list){
            resultList.add(beanCopy(e, type));
        }
        return resultList;
    }
}