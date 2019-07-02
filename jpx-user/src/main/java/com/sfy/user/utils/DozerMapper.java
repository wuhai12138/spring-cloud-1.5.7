package com.sfy.user.utils;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/6/24.
 */
public class DozerMapper {

    private static DozerBeanMapper dozer = new DozerBeanMapper();

    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass)
    {
        List destinationList = new ArrayList<>();
        for (Iterator i$ = sourceList.iterator(); i$.hasNext(); ) { Object sourceObject = i$.next();
            Object destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    public static <T> T map(Object source, Class<T> destinationClass)
    {
        T destinationObject = dozer.map(source, destinationClass);
        return destinationObject;
    }
}
