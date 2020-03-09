package cn.ftf.myblog.dao;

import cn.ftf.myblog.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    User findByUsername(String username);
}
