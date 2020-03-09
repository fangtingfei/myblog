package cn.ftf.myblog.service;

import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Type;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TypeService {
    void addType(String typeName);
    Type findById(Integer id);
    PageInfo pageQuery(QueryPageBean  queryPageBean);
    Type updateType(Integer id,Type type);
    void delete(Integer id);
    List<Type> findAll();
    int updateType(Type type);
    int blogNum(Integer id);
}
