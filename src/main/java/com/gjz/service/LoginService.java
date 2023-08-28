package com.gjz.service;

import com.gjz.pojo.User;
import com.gjz.vo.LoginParam;
import com.gjz.vo.Result;

public interface LoginService {
    Result login(LoginParam loginParam);

    Result logout(String token);

    User checkToken(String token);
}
