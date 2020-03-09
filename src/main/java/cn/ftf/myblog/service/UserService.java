package cn.ftf.myblog.service;

import cn.ftf.myblog.pojo.User;

public interface UserService {
    User checkUser(String username,String password);
}
