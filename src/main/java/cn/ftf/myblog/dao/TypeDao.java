package cn.ftf.myblog.dao;

import cn.ftf.myblog.pojo.Type;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TypeDao {
    Type findById(Integer id);
    void addType(String name);
    List<Type> findAll();
    List<Type> findAll_1();
    void delete(Integer id);
    Integer updateType(Type type);
    Integer blogNum(Integer id);
}
