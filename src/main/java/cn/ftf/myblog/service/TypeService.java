package cn.ftf.myblog.service;

import cn.ftf.myblog.entity.QueryPageBean;
import cn.ftf.myblog.pojo.Type;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TypeService {
    void addType(Type type);
    Type findById(Integer id);
    PageInfo pageQuery(QueryPageBean queryPageBean);
    void delete(Integer id);
    List<Type> findAll();
    List<Type> findAll_1();
    void updateType(Type type);
    int blogNum(Integer id);
}
