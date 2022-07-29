package com.geekplus.flow.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface BaseDao<T> {
    
    void save(T t);

    void save(Map<String, Object> map);
    
    void saveBatch(List<T> list);
    
    int update(T t);

    int update(Map<String, Object> map);

    T queryObject(Object id);

    List<T> queryList(Map<String, Object> map);

    List<T> queryList(Object id);
    
    int queryTotal(Map<String, Object> map);

    int queryTotal();
}
