package com.qingfeng.blog.service;

import com.qingfeng.blog.po.User;

public interface UserService {

    User checkuser(String username, String password);

    User saveUser(String username, String password);
}
