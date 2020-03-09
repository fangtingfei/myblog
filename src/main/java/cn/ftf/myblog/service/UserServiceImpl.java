package cn.ftf.myblog.service;

import cn.ftf.myblog.dao.UserDao;
import cn.ftf.myblog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        User user = userDao.findByUsername(username);
        System.out.println(user);
        return user;
    }

}
